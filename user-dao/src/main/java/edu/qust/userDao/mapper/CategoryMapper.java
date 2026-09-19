package edu.qust.userDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.userDao.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 类别 Mapper
 *
 */
public interface CategoryMapper extends BaseMapper<Category> {

	//标记删除
	void batchDelete(@Param("set") Set<Integer> ids);

}
