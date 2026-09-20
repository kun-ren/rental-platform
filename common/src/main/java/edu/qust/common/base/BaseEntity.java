package edu.qust.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * base entity
 *
 */
@Data
@EqualsAndHashCode
public class BaseEntity {
	/**
	 * ID
	 */
	//@TableId(value = "id",type = IdType.INPUT)
	private Integer id;

	/**
	 * Created-by user ID
	 */
	private Integer addUserId;

	/**
	 * Creation time
	 */
	private LocalDateTime addTime;

	/**
	 * Updated-by user ID
	 */
	private Integer updateUserId;

	/**
	 * Update time
	 */
	private LocalDateTime updateTime;

	/**
	 * Validity flag (0: valid; -1: invalid)
	 */
	private Boolean mark;

	/**
	 * Populate add_user_id, add_time, update_user_id, update_time, and mark during creation
	 *
	 * @param userId Operator
	 */
	public void completeAddParam(Integer userId) {
		LocalDateTime now = LocalDateTime.now();
		this.setAddUserId(userId);
		this.setAddTime(now);
		this.setUpdateUserId(userId);
		this.setUpdateTime(now);
		this.setMark(true);
	}
}
