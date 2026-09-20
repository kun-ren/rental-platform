package edu.qust.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Role enum
 *
 */
public enum RoleEnum {
	/**
	 * ROOT user
	 */
	ROOT(1, "ROOT"),
	/**
	 * Lessor
	 */
	LESSOR(2, "LESSOR"),
	/**
	 * Lessee
	 */
	LESSEE(3, "LESSEE"),
	/**
	 * Guest user
	 */
	GUEST(4, "GUEST");

	/**
	 * Role ID
	 */
	private int id;
	/**
	 * Role name
	 */
	private String name;

	RoleEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * {'key': 'value', 'value': 'RoleEnum'}
	 */
	private static final Map<Integer, RoleEnum> ID_ENUM_MAP;
	private static final Map<String, RoleEnum> NAME_ENUM_MAP;

	static {
		RoleEnum[] enums = RoleEnum.values();
		ID_ENUM_MAP = new HashMap<>(enums.length);
		Arrays.stream(enums).forEach(role -> ID_ENUM_MAP.put(role.id, role));
		NAME_ENUM_MAP = new HashMap<>(enums.length);
		Arrays.stream(enums).forEach(role -> NAME_ENUM_MAP.put(role.name, role));
	}

	public static RoleEnum getById(int id) {
		return ID_ENUM_MAP.get(id);
	}

	public static RoleEnum getByName(String name) {
		return NAME_ENUM_MAP.get(name);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
