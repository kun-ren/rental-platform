package edu.qust.commonInterface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * item status api dto
 *
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemStatusSelectApiDTO implements Serializable {
	/**
	 * status value
	 */
	private Integer value;

	/**
	 * status name
	 */
	private String name;
}