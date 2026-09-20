package edu.qust.common.component.security.sensitive.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.NavigableSet;

/**
 * Sensitive-word filter optimized for lookup speed.<br/>
 * Add a sensitive word: {@link #put(String)} <br/>
 * Filter a sentence: {@link #filter(String, char)} <br/>
 * Get the default singleton:{@link #DEFAULT}
 *
 */
public class SensitiveFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Default singleton using the bundled sensitive-word list
	 */
	public static final SensitiveFilter DEFAULT = new SensitiveFilter(
			new BufferedReader(new InputStreamReader(
					SensitiveFilter.class.getClassLoader().getResourceAsStream("sensitive_words.txt")
					, StandardCharsets.UTF_8)));

	/**
	 * Use a power of two; the dictionary contains roughly 10,000 terms,
	 * so the bucket count should be several times the word count and remain sparse
	 * This increases the chance that an unmatched hash points to null
	 * and speeds up lookups.
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 131072;

	/**
	 * Sparse buckets similar to a HashMap.
	 * Use a two-character hash for lookup.
	 */
	protected SensitiveNode[] nodes = new SensitiveNode[DEFAULT_INITIAL_CAPACITY];

	/**
	 * Construct an empty filter
	 *
	 */
	public SensitiveFilter() {

	}

	/**
	 * Load a dictionary file and build the filter<br/>
	 * The file contains one sensitive term per line<br/>
	 * <b>Note:</b> {@link BufferedReader#close()} is called after reading.<br/>
	 * <b>Note:</b> IOExceptions raised while reading are suppressed
	 *
	 * @param reader
	 */
	public SensitiveFilter(BufferedReader reader) {
		try {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				put(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a sensitive word; discard it when its trimmed length is less than two<br/>
	 * Construction is not the primary performance-sensitive path.
	 * Treat two characters as the minimum word length and group words with the same prefix in one linked node
	 * @param word
	 */
	public boolean put(String word) {
		// Ignore terms shorter than two characters
		if (word == null || word.trim().length() < 2) {
			return false;
		}
		// No additional suffix is needed for a two-character term
		if (word.length() == 2 && word.matches("\\w\\w")) {
			return false;
		}
		StringPointer sp = new StringPointer(word.trim());
		// Calculate the first two characters' hash
		int hash = sp.nextTwoCharHash(0);
		// Calculate the mixed pair value; equal values mean equal characters
		int mix = sp.nextTwoCharMix(0);
		// Convert the hash to a bucket index
		int index = hash & (nodes.length - 1);

		// Read the first node from the bucket
		SensitiveNode node = nodes[index];
		if (node == null) {
			// Create a node when the bucket is empty
			node = new SensitiveNode(mix);
			// Add the word
			node.words.add(sp);
			// Store the node in the bucket
			nodes[index] = node;
		} else {
			// When nodes already exist, find the matching node
			for (; node != null; node = node.next) {
				// Match the node
				if (node.headTwoCharMix == mix) {
					node.words.add(sp);
					return true;
				}
				// Append a node when no existing node matches the same hash and mixed value
				if (node.next == null) {
					new SensitiveNode(mix, node).words.add(sp);
					return true;
				}
			}
		}
		return true;
	}

	/**
	 * Filter sensitive words from a sentence<br/>
	 * When no sensitive word exists, return the original sentence object; callers can test object identity as follows:<br/><code>
	 * String result = filter.filter(sentence, '*');<br/>
	 * if(result != sentence){<br/>
	 * &nbsp;&nbsp;// A sensitive word was found<br/>
	 * }
	 * </code>
	 *
	 * @param sentence Sentence
	 * @param replace  Replacement character for sensitive words
	 * @return Filtered sentence
	 */
	public String filter(String sentence, char replace) {
		// Wrap the text in a StringPointer
		StringPointer sp = new StringPointer(sentence);

		// Indicates whether replacement occurred
		boolean replaced = false;

		// Start of the current match
		int i = 0;
		while (i < sp.length - 2) {
			/*
			 * Step used to move to the next candidate position:
			 * use 1 when unmatched, or the matched word length when matched
			 */
			int step = 1;
			// Calculate the hash of the next two characters
			int hash = sp.nextTwoCharHash(i);
			/*
			 * Get the first node for this hash.
			 * The matching node may not be the first one,
			 * so the following loop checks the chain.
			 */

			//When a node matches, check all of its words
			SensitiveNode node = nodes[hash & (nodes.length - 1)];
			/*
			 * For normal text, the node is usually null.
			 * This significantly improves performance
			 */
			if (node != null) {
				/*
				 * Only calculate the mixed value when a first node exists.
				 * Equal mixed values indicate equal character pairs.
				 * The mixed-value check plays a role similar to equals after hashing in HashMap.
				 */
				int mix = sp.nextTwoCharMix(i);
				/*
				 * Iterate through the nodes. For normal text,
				 * matching mixed values are rare, which improves efficiency
				 */
				outer:
				for (; node != null; node = node.next) {
					/*
					 * For each node, first compare the initial two characters.
					 * If they match, check the node's word list.
					 * This block runs infrequently and is not a primary optimization target.
					 */
					if (node.headTwoCharMix == mix) {
						/*
						 * Find the longest word that fits within the remaining sentence.
						 * For example, if the remaining sentence is "adult movie example",
						 * and the node contains three terms ordered from shortest to longest,
						 * start matching from the longest applicable term
						 *
						 * After the prefix matches, check all prohibited terms with that prefix and use the longest match
						 */
						NavigableSet<StringPointer> desSet = node.words.headSet(sp.substring(i), true);
						if (desSet != null) {
							for (StringPointer word : desSet.descendingSet()) {
								/*
								 * Verify the full word again because a matching prefix alone is insufficient.
								 * For example, a node may contain only one longer term.
								 * The term can still be retrieved but must not match shorter input.
								 */
								if (sp.nextStartsWith(i, word)) {
									// Replace the matched range with the requested content
									sp.fill(i, i + word.length, replace);
									// Skip the replaced range
									step = word.length;
									// Mark that replacement occurred
									replaced = true;
									// Exit the loop and continue at the next while-loop position
									break outer;
								}
							}
						}

					}
				}
			}

			// Move to the next candidate position
			i += step;
		}

		// Return the input when nothing changed to avoid copying the string
		if (replaced) {
			return sp.toString();
		} else {
			return sentence;
		}
	}

}
