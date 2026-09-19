package edu.qust.common.enums;

/**
 * 类别层次枚举
 *
 */
public enum CategoryLevelEnum {
	/**
	 * 一级类别
	 */
	ONE(1, "一级类别"),
	/**
	 * 二级类别
	 */
	TWO(2, "二级类别"),
	/**
	 * 三级类别
	 */
	THREE(3, "三级类别");

	private int code;
	private String name;

	CategoryLevelEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
