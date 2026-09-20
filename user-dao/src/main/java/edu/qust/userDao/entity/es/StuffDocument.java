package edu.qust.userDao.entity.es;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.math.BigDecimal;
import java.util.Date;

/**
 * stuff document (map to es)
 *
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@Document(indexName = "rentalPlatform",type = "stuff")
//@Document(indexName = "rentalPlatform")
public class StuffDocument {
	/**
	 * ID
	 */
	@Id
	private Integer id;

	/**
	 * Category ID
	 */
	private Integer categoryId;

	/**
	 * Item Name
	 */
	private String name;

	/**
	 * Item description
	 */
	private String description;

	/**
	 * Deposit(rmb)
	 */
	private BigDecimal deposit;

	/**
	 * Rental (RMB/day)
	 */
	private BigDecimal rental;

	/**
	 * Item status (0: available; 1: application pending; 2: rented; 3: not offered)
	 */
	private Integer status;

	/**
	 * Image ID
	 */
	private String pictureId;

	/**
	 * Item owner ID
	 */
	private Integer userId;

	/**
	 * Created-by user ID
	 */
	private Integer addUserId;

	/**
	 * Creation time
	 */
	@Field(format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	private Date addTime;

	/**
	 * Updated-by user ID
	 */
	private Integer updateUserId;

	/**
	 * Update time
	 */
	@Field(format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	private Date updateTime;

	/**
	 * Validity flag (0: valid; -1: invalid)
	 */
	private Boolean mark;
}
