package edu.qust.common.enums;

/**
 * Enable/disable enum
 *
 */
public enum EnableEnum {
	/**
	 * Yes
	 */
	YES(1),
	/**
	 * No
	 */
	NO(0);

	private int value;

	EnableEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
