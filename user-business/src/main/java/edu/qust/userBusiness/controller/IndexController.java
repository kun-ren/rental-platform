package edu.qust.userBusiness.controller;

import edu.qust.userBusiness.common.WebConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * 基础控制器
 *
 */
@Slf4j
@Controller
@RequestMapping
public class IndexController {

	/**
	 * 首页
	 *
	 * @return java.lang.String
	 */
	@GetMapping
	public String index(Model model, HttpSession session) {
		// 获取菜单
		model.addAttribute("menuVOList", session.getAttribute(WebConstant.Session.MENU_VO_LIST_SESSION_KEY));
		return "index";
	}

}
