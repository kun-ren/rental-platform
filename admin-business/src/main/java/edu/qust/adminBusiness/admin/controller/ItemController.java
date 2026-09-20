package edu.qust.adminBusiness.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.common.base.BaseController;
import edu.qust.common.base.Response;
import edu.qust.commonInterface.CategoryServiceApi;
import edu.qust.commonInterface.ItemServiceApi;
import edu.qust.commonInterface.dto.CategorySelectApiDTO;
import edu.qust.commonInterface.param.ItemDailyStatsApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rental administration
 *
 */
@Slf4j
@Controller
@RequestMapping("/items")
public class ItemController extends BaseController {
	@Reference
	private ItemServiceApi itemServiceApi;
	@Reference
	private CategoryServiceApi categoryServiceApi;

	/**
	 * Open the rental-list page
	 *
	 * @return page
	 */
	@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("itemApiDTOList", itemServiceApi.listItemApiDTO());
		return "item_index";
	}

	@ResponseBody
	@PatchMapping("/{id}/status/{status}")
	public Response patchStatus(@PathVariable Integer id, @PathVariable Integer status) {
		log.info("patch item status, id={}, status={}", id, status);
		// Get the current userID
		final int userId = -1;
		return itemServiceApi.patchStatus(id, status,userId);
	}

	@GetMapping("/daily-stats")
	public String dailyStatsPage(Model model) {
		List<CategorySelectApiDTO> categorySelectApiDTOList =categoryServiceApi.listCategorySelectApiDTO();
		Map<Integer, String> categorySelectIdNameMap = new HashMap<>();
		for (CategorySelectApiDTO categorySelectApiDTO : categorySelectApiDTOList) {
			categorySelectIdNameMap.put(categorySelectApiDTO.getId(), categorySelectApiDTO.getName());
		}
		model.addAttribute("categorySelectIdNameMap", categorySelectIdNameMap);
		model.addAttribute("statusSelectList", itemServiceApi.listItemStatusSelectApiDTO());
		return "item_daily_stats";
	}

	@PostMapping("/daily-stats")
	public String dailyStats(ItemDailyStatsApiParam itemDailyStatsApiParam, RedirectAttributes redirectAttributes) {
		itemDailyStatsApiParam.setBeginAddDate(StringUtils.trimToNull(itemDailyStatsApiParam.getBeginAddDate()));
		itemDailyStatsApiParam.setEndAddDate(StringUtils.trimToNull(itemDailyStatsApiParam.getEndAddDate()));
		redirectAttributes.addFlashAttribute("dailyStatsList", itemServiceApi.listItemDailyStatsApiDTO(itemDailyStatsApiParam));
		redirectAttributes.addFlashAttribute("itemDailyStatsApiParam", itemDailyStatsApiParam);
		return redirect("daily-stats");
	}
}
