package edu.qust.userBusiness.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.qust.common.component.security.sensitive.EnableSensitive;
import edu.qust.common.component.security.xss.EnableXss;
import edu.qust.userBusiness.controller.base.WebBaseController;
import edu.qust.userService.CategoryService;
import edu.qust.userService.ItemService;
import edu.qust.userService.StuffService;
import edu.qust.userService.param.StuffParam;
import edu.qust.userService.vo.StuffInVO;
import edu.qust.userService.vo.StuffSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import edu.qust.common.base.Response;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Item controller
 *
 */
@Slf4j
@Controller
@RequestMapping("/stuffs")
public class StuffController extends WebBaseController {
	@Resource
	private StuffService stuffService;
	@Resource
	private ItemService itemService;
	@Resource
	private CategoryService categoryService;

	/**
	 * Start Renting
	 *
	 * <pre>
	 *     Hide items marked "Not Offered"
	 *     Show the expected return date for rented items
	 *     Show the rent action for available items
	 * </pre>
	 *
	 * @return JSON
	 */
	@GetMapping("/vue/in")
	@ResponseBody
	public String startRentIn() {
		return JSON.toJSONString(stuffService.listStuffInVO());
	}


	//@return page
	@GetMapping("/in")
	public String startRentIn(Model model) {
		model.addAttribute("stuffInVOList", stuffService.listStuffInVO());
		return "start_rent_in";
	}
	/**
	 * List an Item
	 *
	 * @return page
	 */
	@GetMapping("/out/start")
	public String startRentOut() {
		return "start_rent_out";
	}

	/**
	 * My Listings
	 *
	 * <pre>
	 *     Show the stop-listing action for available items
	 * </pre>
	 *
	 * @return page
	 */
	@GetMapping("/out")
	public String myRentOut(Model model, Authentication authentication, HttpSession session) {
		model.addAttribute("stuffOutVOList", stuffService.listStuffOutVO(currentNonRootUserId(authentication, session)));
		return "my_rent_out";
	}

	@GetMapping("/vue/out")
	@ResponseBody
	public String getRentOut( HttpSession session){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return JSON.toJSONString(stuffService.listStuffOutVO(currentNonRootUserId(authentication, session)));
	}

	/**
	 * Submit a new listing
	 *
	 * @param stuffParam stuffParam
	 * @return Response
	 */
	@ResponseBody
	@PostMapping("/out")
	@EnableXss
	@EnableSensitive
	public Response addRentOut(@RequestBody StuffParam stuffParam, HttpSession session) {
		stuffParam.setUserId(currentUserId(session));
		log.info("Submit a new listingstuffParam: {}", stuffParam);
		return stuffService.add(stuffParam);
	}

	/**
	 * Stop Listing
	 *
	 * @param id stuff id
	 * @return Response
	 */
	@ResponseBody
	@PostMapping("/{id}/cancel-rent")
	public Response cancelRent(@PathVariable Integer id, HttpSession session) {
		return stuffService.cancelRent(id, currentUserId(session));
	}

	/**
	 * Rent
	 *
	 * @param id stuff id
	 * @param rentDay rent day
	 * @return Response
	 */
	@ResponseBody
	@PostMapping("/{id}/rent")
	public Response rent(@PathVariable Integer id,Integer rentDay , HttpSession session) {
		return itemService.rent(id, currentUserId(session), rentDay);
	}

	/**
	 * Open the search page
	 *
	 * @param model                model
	 * @param stuffSearchVoListStr stuffSearchVoListStr
	 * @return java.lang.String
	 */
	@GetMapping("/search")
	public String searchPage(Model model, @ModelAttribute("stuffSearchVoListStr") String stuffSearchVoListStr) throws IOException {
		model.addAttribute("categorySelectVoList", categoryService.listCategorySelectVO());
		if (StringUtils.isNotBlank(stuffSearchVoListStr)) {
			model.addAttribute("stuffSearchVoList",
					new ObjectMapper().readValue(stuffSearchVoListStr, StuffSearchVO[].class));
		}
		return "search";
	}

	/**
	 * Search
	 *
	 * @param categoryId         categoryId
	 * @param name               name
	 * @param desc               desc
	 * @param redirectAttributes redirectAttributes
	 * @return java.lang.String
	 */
	@PostMapping("/search")
	public String search(Integer categoryId, String name, String desc, RedirectAttributes redirectAttributes) throws JsonProcessingException {
		redirectAttributes.addFlashAttribute("stuffSearchVoListStr",
				new ObjectMapper().writeValueAsString(stuffService.searchByCategoryAndNameAndDesc(categoryId, name, desc)));
		redirectAttributes.addFlashAttribute("nameCondition", name);
		redirectAttributes.addFlashAttribute("descCondition", desc);
		return redirect("search");
	}
}
