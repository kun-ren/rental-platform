package edu.qust.userDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.userDao.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * Category mapper
 *
 */
public interface CategoryMapper extends BaseMapper<Category> {

	//Soft-delete
	void batchDelete(@Param("set") Set<Integer> ids);

}
