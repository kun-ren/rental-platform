package edu.qust.adminBusiness.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Base controller
 *
 */
@Slf4j
@Controller
@RequestMapping
public class IndexController {

	/**
	 * Home page
	 *
	 * @return java.lang.String
	 */
	@GetMapping
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login(){ return "login"; }
}
