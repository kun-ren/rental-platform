package edu.qust.common.enums;

/**
 * 开关枚举
 *
 */
public enum EnableEnum {
	/**
	 * 肯定
	 */
	YES(1),
	/**
	 * 否定
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
