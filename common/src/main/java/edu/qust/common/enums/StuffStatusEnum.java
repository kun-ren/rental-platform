package edu.qust.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Item availability enum
 *
 */
public enum StuffStatusEnum {
	/**
	 * Not Offered
	 */
	NOT(3, "Not Offered"),
	/**
	 * Available
	 */
	HAVE_NOT(0, "Available"),
	/**
	 * Application Pending
	 */
	APPLY(1, "Application Pending"),
	/**
	 * Rented
	 */
	ALREADY(2, "Rented");

	private int code;
	private String name;

	StuffStatusEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	/**
	 * {'key': 'value', 'value': 'StuffStatusEnum'}
	 */
	private static final Map<Integer, StuffStatusEnum> MAP;

	static {
		StuffStatusEnum[] enums = StuffStatusEnum.values();
		MAP = new HashMap<>(enums.length);
		Arrays.stream(enums).forEach(type -> MAP.put(type.code, type));
	}

	public static StuffStatusEnum getByValue(int value) {
		return MAP.get(value);
	}
}
