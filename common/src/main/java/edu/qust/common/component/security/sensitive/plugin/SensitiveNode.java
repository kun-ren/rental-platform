package edu.qust.common.component.security.sensitive.plugin;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * Sensitive-word node containing every word that starts with the same two characters
 *
 */
public class SensitiveNode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Mixed value for the first two characters; equal values indicate equal character pairs
	 */
	protected final int headTwoCharMix;

	/**
	 * Words beginning with this character pair
	 */
	protected final TreeSet<StringPointer> words = new TreeSet<StringPointer>();

	/**
	 * Next node
	 */
	protected SensitiveNode next;

	public SensitiveNode(int headTwoCharMix) {
		this.headTwoCharMix = headTwoCharMix;
	}

	public SensitiveNode(int headTwoCharMix, SensitiveNode parent) {
		this.headTwoCharMix = headTwoCharMix;
		parent.next = this;
	}

}
