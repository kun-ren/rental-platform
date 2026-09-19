package edu.qust.commonInterface;

import edu.qust.common.base.Response;
import edu.qust.commonInterface.dto.ItemApiDTO;
import edu.qust.commonInterface.dto.ItemDailyStatsApiDTO;
import edu.qust.commonInterface.dto.ItemStatusSelectApiDTO;
import edu.qust.commonInterface.param.ItemDailyStatsApiParam;

import java.util.List;

/**
 * item service api
 *
 */
public interface ItemServiceApi {
	List<ItemApiDTO> listItemApiDTO();

	Response patchStatus(Integer itemId, Integer status, int userId);

	List<ItemDailyStatsApiDTO> listItemDailyStatsApiDTO(ItemDailyStatsApiParam itemDailyStatsApiParam);
	
	List<ItemStatusSelectApiDTO> listItemStatusSelectApiDTO();
}
