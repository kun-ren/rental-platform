package edu.qust.common.enums;

/**
 * Category-level enum
 *
 */
public enum CategoryLevelEnum {
	/**
	 * Level-One Category
	 */
	ONE(1, "Level-One Category"),
	/**
	 * Level-Two Category
	 */
	TWO(2, "Level-Two Category"),
	/**
	 * Level-Three Category
	 */
	THREE(3, "Level-Three Category");

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
