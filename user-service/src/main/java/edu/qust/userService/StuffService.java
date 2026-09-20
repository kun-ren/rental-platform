package edu.qust.userService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qust.common.component.security.xss.EnableXss;
import edu.qust.userDao.entity.ProductRating;
import edu.qust.userService.param.StuffParam;
import edu.qust.userService.vo.StuffInVO;
import edu.qust.userService.vo.StuffOutVO;
import edu.qust.userService.vo.StuffSearchVO;
import edu.qust.common.base.Response;
import edu.qust.common.enums.StuffStatusEnum;
import edu.qust.common.util.BeanUtil;
import edu.qust.userDao.dto.StuffDTO;
import edu.qust.userDao.entity.Stuff;
import edu.qust.userDao.entity.es.StuffDocument;
import edu.qust.userDao.mapper.StuffMapper;
import edu.qust.userDao.repository.StuffRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import edu.qust.common.enums.EnableEnum;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Item service
 *
 */
@Slf4j
@Service
public class StuffService extends ServiceImpl<StuffMapper, Stuff> implements IService<Stuff> {
	@Resource
	private StuffMapper stuffMapper;
	@Resource
	private StuffRepository stuffRepository;
	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductRatingService productRatingService;

	public List<StuffInVO> listStuffInVO() {
		List<StuffDTO> stuffDTOList = stuffMapper.listStuffDTO();
		List<ProductRating> ratingList = productRatingService.list();
		Map<Integer, List<ProductRating>> map = ratingList.stream().collect(Collectors.groupingBy(ProductRating::getProductId));
		Map<Integer,Double> averageScore = new HashMap<>();
		map.forEach( (k,v) ->{
			Double value = v.stream().mapToDouble(ProductRating::getScore).average().getAsDouble();
			averageScore.put(k,value);
		});
		return stuffDTOList.stream().map(
				s -> new StuffInVO()
						.setId(s.getStuffId())
						.setCategoryName(s.getCategoryName())
						.setStuffName(s.getStuffName())
						.setOwnerName(s.getOwnerName())
						.setDeposit(s.getDeposit())
						.setRental(s.getRental())
						.setStatus(StuffStatusEnum.getByValue(s.getStatus()))
						.setShouldReturnDate(s.getCreateTime() == null ? null : s.getCreateTime().plusDays(s.getRentDay()))
						.setRate(averageScore.get(s.getStuffId()) == null ? 0 : averageScore.get(s.getStuffId()))
		).collect(Collectors.toList());
	}



	public List<StuffOutVO> listStuffOutVO(Integer userId) {
		List<StuffDTO> stuffDTOList = stuffMapper.listOutStuffDTO(userId);
		return stuffDTOList.stream().map(
				s -> new StuffOutVO()
						.setId(s.getStuffId())
						.setCategoryName(s.getCategoryName())
						.setStuffName(s.getStuffName())
						.setRenterName(s.getRenterName())
						.setDeposit(s.getDeposit())
						.setRental(s.getRental())
						.setStatus(StuffStatusEnum.getByValue(s.getStatus()))
						.setShouldReturnDate(s.getCreateTime() == null ? null : s.getCreateTime().plusDays(s.getRentDay()))
		).collect(Collectors.toList());
	}

	public Response add(StuffParam stuffParam) {
		Stuff stuff = BeanUtil.map(stuffParam, Stuff.class);
		stuff.setAddUserId(stuff.getUserId());
		stuff.setUpdateUserId(stuff.getUserId());
		boolean success = this.save(stuff);
		log.info("a stuff saved: {}", stuff);
		if (success) {
			return Response.SUCCESS;
		} else {
			return Response.FAIL;
		}
	}

	public Response cancelRent(Integer stuffId, int userId) {
		if (this.count(
				new QueryWrapper<Stuff>().lambda()
						.eq(Stuff::getId, stuffId)
						.eq(Stuff::getMark, EnableEnum.YES.getValue())
		) <= 0) {
			return Response.fail("The item does not exist");
		}
		Stuff stuff = new Stuff();
		stuff.setId(stuffId);
		stuff.setStatus(StuffStatusEnum.NOT.getCode());
		stuff.setUpdateUserId(userId);
		boolean success = this.updateById(stuff);
		log.info("a stuff updated: {}", stuff);
		if (success) {
			return Response.SUCCESS;
		} else {
			return Response.FAIL;
		}
	}

	/**
	 * find by name and description and categoryId
	 *
	 * @param name       Item Name
	 * @param desc       Item description
	 * @param categoryId Category ID
	 * @return StuffSearchVO StuffSearchVO
	 */
	public List<StuffSearchVO> searchByCategoryAndNameAndDesc(Integer categoryId, String name, String desc) {
		Map<Integer, String> categoryIdNameMap = categoryService.getIdNameMap();
		List<StuffDocument> stuffDocumentList = stuffRepository.findByNameAndDescriptionAndCategoryId(
				StringUtils.trimToNull(name), StringUtils.trimToNull(desc), categoryId);
		List<StuffSearchVO> stuffSearchVOList = new ArrayList<>();
		for (StuffDocument stuffDocument : stuffDocumentList) {
			stuffSearchVOList.add(
					new StuffSearchVO()
							.setId(stuffDocument.getId())
							.setCategoryName(categoryIdNameMap.get(stuffDocument.getCategoryId()))
							.setStuffName(stuffDocument.getName())
							.setStuffDescription(stuffDocument.getDescription())
							.setDeposit(stuffDocument.getDeposit())
							.setRental(stuffDocument.getRental())
							.setStatus(StuffStatusEnum.getByValue(stuffDocument.getStatus()))
			);
		}
		return stuffSearchVOList;
	}

	public Stuff getById(Integer id) {
		return this.getOne(
				new QueryWrapper<Stuff>().lambda()
						.eq(Stuff::getId, id)
						.eq(Stuff::getMark, EnableEnum.YES.getValue())
		);
	}
}
