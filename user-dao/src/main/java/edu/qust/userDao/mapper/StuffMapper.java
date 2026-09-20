package edu.qust.userDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.userDao.dto.StuffDTO;
import edu.qust.userDao.entity.Stuff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Item mapper
 */
public interface StuffMapper extends BaseMapper<Stuff> {

	List<StuffDTO> listStuffDTO();

	List<StuffDTO> listOutStuffDTO(@Param("userId") Integer userId);
}
