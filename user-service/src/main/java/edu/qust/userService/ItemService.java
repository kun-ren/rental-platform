package edu.qust.userService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.userService.param.ItemDailyStatsParam;
import edu.qust.userService.vo.PersonalItemVO;
import edu.qust.common.base.Response;
import edu.qust.common.enums.EnableEnum;
import edu.qust.common.enums.ItemStatusEnum;
import edu.qust.common.enums.StuffStatusEnum;
import edu.qust.common.util.BeanUtil;
import edu.qust.userDao.dto.ItemDailyStatsDTO;
import edu.qust.userDao.entity.Item;
import edu.qust.userDao.entity.Stuff;
import edu.qust.userDao.mapper.ItemMapper;
import edu.qust.userDao.query.ItemDailyStatsQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rental-record service
 *
 */
@Slf4j
@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IService<Item> {
	@Resource
	private ItemMapper itemMapper;
	//@Resource
	@Resource
	private StuffService stuffService;

	public List<PersonalItemVO> listPersonItemVO(Integer userId) {
		return itemMapper.listItemDTO(userId).stream()
				.map(
						i -> new PersonalItemVO()
								.setItemId(i.getItemId())
								.setStuffName(i.getStuffName())
								.setApplyTime(i.getApplyTime())
								.setApprovalTime(i.getApprovalTime())
								.setPayTime(i.getPayTime())
								.setRentDay(i.getRentDay())
								.setEndTime(i.getEndTime())
								.setStatus(ItemStatusEnum.getByValue(i.getStatus()))
				)
				.collect(Collectors.toList());
	}

	public Response 	rent(Integer stuffId, int userId, Integer rentDay) {
		// update stuff
		if (stuffService.count(
				new QueryWrapper<Stuff>().lambda()
						.eq(Stuff::getId, stuffId)
						.eq(Stuff::getMark, EnableEnum.YES.getValue())
		) <= 0) {
			return Response.fail("The item does not exist");
		}
		Stuff stuff = new Stuff();
		stuff.setId(stuffId);
		stuff.setStatus(StuffStatusEnum.APPLY.getCode());
		stuff.setUpdateUserId(userId);
		boolean updateStuffSuccess = stuffService.updateById(stuff);
		log.info("a stuff updated: {}", stuff);
		if (!updateStuffSuccess) {
			return Response.fail("Failed to update the item");
		}
		// insert item
		Item item = new Item();
		item.setUserId(userId);
		item.setStuffId(stuffId);
		item.setRentDay(rentDay);
		item.setApplyTime(LocalDateTime.now());
		item.setStatus(ItemStatusEnum.APPLYING.getValue());
		item.setAddDate(LocalDate.now());
		item.setAddUserId(userId);
		item.setUpdateUserId(userId);
		boolean success = this.save(item);
		log.info("an item saved: {}", item);
		if(success) {
			return Response.SUCCESS;
		} else {
			return Response.FAIL;
		}
	}

	public Response cancelApply(Integer itemId, int userId) {
		Item item = this.getOne(
				new QueryWrapper<Item>().lambda()
						.eq(Item::getId, itemId)
						.eq(Item::getMark, EnableEnum.YES.getValue())
		);
		if(item == null) {
			return Response.fail("The rental record does not exist");
		}
		Item updateItem = new Item();
		updateItem.setId(itemId);
		updateItem.setUpdateUserId(userId);
		updateItem.setMark(false);
		boolean updateItemSuccess = this.updateById(updateItem);
		if(!updateItemSuccess) {
			return Response.fail("Failed to update the rental record");
		}
		log.info("an item updated: {}", updateItem);
		Stuff updateStuff = new Stuff();
		updateStuff.setId(item.getStuffId());
		updateStuff.setStatus(StuffStatusEnum.HAVE_NOT.getCode());
		updateStuff.setUpdateUserId(userId);
		boolean updateStuffSuccess = stuffService.updateById(updateStuff);
		if(!updateStuffSuccess) {
			return Response.fail("Failed to update the item");
		}
		log.info("a stuff updated: {}", updateStuff);
		return Response.SUCCESS;
	}

	private Item getById(Integer id) {
		return this.getOne(
				new QueryWrapper<Item>().lambda()
						.eq(Item::getId, id)
						.eq(Item::getMark, EnableEnum.YES.getValue())
		);
	}

	public Response patchStatus(Integer itemId, Integer status, int userId) {
		if (ItemStatusEnum.APPLYING.getValue().equals(status)) {
			return Response.fail("Cannot update the rental record because the target status is invalid");
		}
		Item item = this.getById(itemId);
		if (item == null) {
			return Response.fail("The rental record does not exist");
		}
		LocalDateTime now = LocalDateTime.now();
		Item updateItem = new Item();
		updateItem.setId(itemId);
		updateItem.setUpdateUserId(userId);
		updateItem.setStatus(status);
		switch (status) {
			case 1:
			case 2:
				updateItem.setApprovalTime(now);
				break;
			case 4:
				updateItem.setEndTime(now);
				break;
			default:
		}
		boolean updateItemSuccess = this.updateById(updateItem);
		if (!updateItemSuccess) {
			return Response.fail("Failed to update the rental record");
		}
		log.info("an item updated: {}", updateItem);
		Stuff updateStuff = new Stuff();
		updateStuff.setId(item.getStuffId());
		switch (status) {
			case 2:
				updateStuff.setStatus(StuffStatusEnum.ALREADY.getCode());
				break;
			case 1:
			case 4:
				updateStuff.setStatus(StuffStatusEnum.HAVE_NOT.getCode());
				break;
			default:
		}
		updateStuff.setUpdateUserId(userId);
		boolean updateStuffSuccess = stuffService.updateById(updateStuff);
		if (!updateStuffSuccess) {
			return Response.fail("Failed to update the item");
		}
		log.info("a stuff updated: {}", updateStuff);
		return Response.SUCCESS;
	}

	public List<ItemDailyStatsDTO> listItemDailyStatsDTO(ItemDailyStatsParam itemDailyStatsParam) {
		return itemMapper.listItemDailyStatsDTO(BeanUtil.map(itemDailyStatsParam, ItemDailyStatsQuery.class));
	}

	public void finishPay(Integer itemId, Integer userId) {
		Item updateItem = new Item();
		updateItem.setId(itemId);
		updateItem.setUpdateUserId(userId);
		updateItem.setStatus(ItemStatusEnum.RENTING.getValue());
		updateItem.setPayTime(LocalDateTime.now());
		this.updateById(updateItem);
	}

	public BigDecimal getTotalDepositAndRental(Integer id) {
		Item item = this.getById(id);
		Stuff stuff = stuffService.getById(item.getStuffId());
		BigDecimal totalRental = stuff.getRental().multiply(new BigDecimal(item.getRentDay()));
		return totalRental.add(stuff.getDeposit());
	}
}
