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
 * 类别管理 controller
 *
 */
@Slf4j
@Controller
	@RequestMapping("/categories")
public class CategoryController extends BaseController {
	@Reference
	private CategoryServiceApi categoryServiceApi;

	/**
	 * 跳转到类别列表首页
	 *
	 * @return page
	 */
	@GetMapping("/index")
	public String index() {
		return "category_index";
	}

	/**
	 * 获取所有类别信息
	 *
	 * @return Response
	 */
	@GetMapping
	@ResponseBody
	public Response list() {
		return Response.success(categoryServiceApi.listCategoryApiDTO());
	}

	/**
	 * 修改类别
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
	 * 删除类别
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
	 * 添加类别
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
