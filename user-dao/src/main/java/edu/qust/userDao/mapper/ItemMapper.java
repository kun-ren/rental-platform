package edu.qust.userDao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.qust.userDao.dto.ItemDTO;
import edu.qust.userDao.dto.ItemDailyStatsDTO;
import edu.qust.userDao.entity.Item;
import edu.qust.userDao.query.ItemDailyStatsQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Rental-record mapper
 *
 */
public interface ItemMapper extends BaseMapper<Item> {

	List<ItemDTO> listItemDTO(@Param("userId") Integer userId);

	List<ItemDTO> listCompleteItemDTO();

	List<ItemDailyStatsDTO> listItemDailyStatsDTO(ItemDailyStatsQuery itemDailyStatsQuery);
}
