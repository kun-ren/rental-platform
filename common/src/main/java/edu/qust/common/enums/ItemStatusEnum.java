package edu.qust.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 */
public enum ItemStatusEnum implements IEnum<Integer> {
	/**
	 * Applying
	 */
	APPLYING(0, "Applying"),
	/**
	 * Rejected
	 */
	DISAPPROVED(1, "Rejected"),
	/**
	 * Awaiting Payment
	 */
	UNPAID(2, "Awaiting Payment"),
	/**
	 * Renting
	 */
	RENTING(3, "Renting"),
	/**
	 * Returned
	 */
	RETURNED(4, "Returned");

	private int value;
	private String name;

	ItemStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	/**
	 * {'key': 'value', 'value': 'ItemStatusEnum'}
	 */
	private static final Map<Integer, ItemStatusEnum> MAP;

	static {
		ItemStatusEnum[] enums = ItemStatusEnum.values();
		MAP = new HashMap<>(enums.length);
		Arrays.stream(enums).forEach(type -> MAP.put(type.value, type));
	}

	public static ItemStatusEnum getByValue(int value) {
		return MAP.get(value);
	}
}
