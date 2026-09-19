package edu.qust.adminBusiness.admin.controller;

import edu.qust.adminService.UserService;
import edu.qust.common.base.BaseController;
import edu.qust.common.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 用户表 controller
 *
 */
@Slf4j
@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
	@Resource
	private UserService userService;

	@GetMapping
	@ResponseBody
	public Response list() {
		return Response.success(userService.listUsers());
	}

}
