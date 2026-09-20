package edu.qust.adminBusiness.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.common.base.BaseController;
import edu.qust.common.base.Response;
import edu.qust.commonInterface.CategoryServiceApi;
import edu.qust.commonInterface.param.CategoryApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Category Management controller
 *
 */
@Slf4j
@Controller
	@RequestMapping("/categories")
public class CategoryController extends BaseController {
	@Reference
	private CategoryServiceApi categoryServiceApi;

	/**
	 * Open the category-list page
	 *
	 * @return page
	 */
	@GetMapping("/index")
	public String index() {
		return "category_index";
	}

	/**
	 * Get all category information
	 *
	 * @return Response
	 */
	@GetMapping
	@ResponseBody
	public Response list() {
		return Response.success(categoryServiceApi.listCategoryApiDTO());
	}

	/**
	 * Update a category
	 * @param id category id
	 * @param categoryApiParam categoryApiParam
	 * @return Response
	 */
	@ResponseBody
	@PatchMapping("/{id}")
	public Response modify(@PathVariable Integer id, @RequestBody CategoryApiParam categoryApiParam) {
		categoryApiParam.setId(id);
		log.info("modify categoryApiParam: {}", categoryApiParam);
		return categoryServiceApi.modify(categoryApiParam);
	}

	/**
	 * Delete a category
	 * @param id category id
	 * @return Response
	 */
	@ResponseBody
	@DeleteMapping("/{id}")
	public Response delete(@PathVariable Integer id) {
		log.info("delete category id: {}", id);
		return categoryServiceApi.delete(id);
	}

	/**
	 * Add Category
	 *
	 * @param categoryApiParam categoryApiParam
	 * @return Response
	 */
	@PostMapping
	@ResponseBody
	public Response add(@RequestBody CategoryApiParam categoryApiParam) {
		log.info("add categoryApiParam: {}", categoryApiParam);
		return categoryServiceApi.add(categoryApiParam);
	}
}
