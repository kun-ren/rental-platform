package edu.qust.commonInterface.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * CategorySelect api dto
 *
 */
@Data
@Accessors(chain = true)
public class CategorySelectApiDTO implements Serializable {
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 类别名称
	 */
	private String name;
}
