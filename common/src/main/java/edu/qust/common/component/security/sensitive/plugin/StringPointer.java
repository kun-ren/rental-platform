package edu.qust.common.component.security.sensitive.plugin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Undocumented methods behave like {@link String}<br/>
 * <b>Note:</b> no bounds or other safety checks are performed<br/>
 * Can be used as a key in {@link HashMap} and {@link TreeMap}
 *
 */
public class StringPointer implements Serializable, CharSequence, Comparable<StringPointer> {

	private static final long serialVersionUID = 1L;

	protected final char[] value;

	protected final int offset;

	protected final int length;

	private int hash = 0;

	public StringPointer(String str) {
		value = str.toCharArray();
		offset = 0;
		length = value.length;
	}

	public StringPointer(char[] value, int offset, int length) {
		this.value = value;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Calculate the hash of the two characters starting at this position
	 *
	 * @param i From 0 to length - 2
	 * @return Hash value
	 */
	public int nextTwoCharHash(int i) {
		return 31 * value[offset + i] + value[offset + i + 1];
	}

	/**
	 * Combine the two characters starting at this position into one int value<br/>
	 * Equal int values indicate equal character pairs
	 *
	 * @param i From 0 to length - 2
	 * @return Integer value
	 */
	public int nextTwoCharMix(int i) {
		return (value[offset + i] << 16) | value[offset + i + 1];
	}

	/**
	 * Whether the substring starting here begins with the supplied word
	 *
	 * @param i    From 0 to length - 2
	 * @param word Word to test
	 * @return Whether it matches
	 */
	public boolean nextStartsWith(int i, StringPointer word) {
		// Check whether the range exceeds the length
		if (word.length > length - i) {
			return false;
		}
		// Compare from the end
		for (int c = word.length - 1; c >= 0; c--) {
			if (value[offset + i + c] != word.value[word.offset + c]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Fill (replace)
	 *
	 * @param begin    Start at this position, inclusive
	 * @param end      End at this position, exclusive
	 * @param fillWith Fill the range with this replacement character
	 */
	public void fill(int begin, int end, char fillWith) {
		for (int i = begin; i < end; i++) {
			value[offset + i] = fillWith;
		}
	}

	public int length() {
		return length;
	}

	public char charAt(int i) {
		return value[offset + i];
	}

	public StringPointer substring(int begin) {
		return new StringPointer(value, offset + begin, length - begin);
	}

	public StringPointer substring(int begin, int end) {
		return new StringPointer(value, offset + begin, end - begin);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return substring(start, end);
	}

	public String toString() {
		return new String(value, offset, length);
	}

	public int hashCode() {
		int h = hash;
		if (h == 0 && length > 0) {
			for (int i = 0; i < length; i++) {
				h = 31 * h + value[offset + i];
			}
			hash = h;
		}
		return h;
	}

	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof StringPointer) {
			StringPointer that = (StringPointer) anObject;
			if (length == that.length) {
				char v1[] = this.value;
				char v2[] = that.value;
				for (int i = 0; i < this.length; i++) {
					if (v1[this.offset + i] != v2[that.offset + i]) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(StringPointer that) {
		int len1 = this.length;
		int len2 = that.length;
		int lim = Math.min(len1, len2);
		char v1[] = this.value;
		char v2[] = that.value;

		int k = 0;
		while (k < lim) {
			char c1 = v1[this.offset + k];
			char c2 = v2[that.offset + k];
			if (c1 != c2) {
				return c1 - c2;
			}
			k++;
		}
		return len1 - len2;
	}

}
