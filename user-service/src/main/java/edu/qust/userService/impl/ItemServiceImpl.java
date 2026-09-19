package edu.qust.userService.impl;

import com.alibaba.dubbo.config.annotation.Service;
import edu.qust.userService.ItemService;
import edu.qust.userService.param.ItemDailyStatsParam;
import edu.qust.common.enums.ItemStatusEnum;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.ItemServiceApi;
import edu.qust.commonInterface.dto.ItemApiDTO;
import edu.qust.commonInterface.dto.ItemDailyStatsApiDTO;
import edu.qust.commonInterface.dto.ItemStatusSelectApiDTO;
import edu.qust.commonInterface.param.ItemDailyStatsApiParam;
import edu.qust.userDao.mapper.ItemMapper;
import lombok.extern.slf4j.Slf4j;
import edu.qust.common.base.Response;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ItemService Impl
 *
 */
@Slf4j
@Service
public class ItemServiceImpl implements ItemServiceApi {
	@Resource
	private ItemMapper itemMapper;
	@Resource
	private ItemService itemService;

	@Override
	public List<ItemApiDTO> listItemApiDTO() {
		return itemMapper.listCompleteItemDTO().stream()
				.map(
						i -> new ItemApiDTO()
								.setItemId(i.getItemId())
								.setStuffName(i.getStuffName())
								.setApplyTime(i.getApplyTime())
								.setApprovalTime(i.getApprovalTime())
								.setPayTime(i.getPayTime())
								.setRentDay(i.getRentDay())
								.setEndTime(i.getEndTime())
								.setStatus(ItemStatusEnum.getByValue(i.getStatus()))
								.setOwnerName(i.getOwnerName())
								.setRenterName(i.getRenterName())
				)
				.collect(Collectors.toList());
	}

	@Override
	public Response patchStatus(Integer itemId, Integer status, int userId) {
		return itemService.patchStatus(itemId, status, userId);
	}

	@Override
	public List<ItemDailyStatsApiDTO> listItemDailyStatsApiDTO(ItemDailyStatsApiParam itemDailyStatsApiParam) {
		return itemService.listItemDailyStatsDTO(BeanUtil.map(itemDailyStatsApiParam, ItemDailyStatsParam.class)).stream()
				.map(dto -> BeanUtil.map(dto, ItemDailyStatsApiDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<ItemStatusSelectApiDTO> listItemStatusSelectApiDTO() {
		return Arrays.stream(ItemStatusEnum.values())
				.map(status -> new ItemStatusSelectApiDTO(status.getValue(), status.getName()))
				.collect(Collectors.toList());
	}


}
