package edu.qust.userBusiness.controller;

import edu.qust.common.base.BaseController;
import edu.qust.common.base.Response;
import edu.qust.userService.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Category controller
 *
 */
@Slf4j
@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {
	@Resource
	private CategoryService categoryService;

	/**
	 * Get all category view objects
	 *
	 * @return category VO list
	 */
	@ResponseBody
	@GetMapping
	public Response list() {
		return Response.success(categoryService.listCategoryVO());
	}
}
