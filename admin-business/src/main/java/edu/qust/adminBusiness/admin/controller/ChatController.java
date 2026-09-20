package edu.qust.adminBusiness.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Chat controller
 *
 */
@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController {

	/**
	 * Open the chat page
	 */
	@GetMapping("/index")
	public String index(HttpServletRequest request, Model model) throws UnknownHostException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("username", authentication.getName());
		model.addAttribute("webSocketUrl","ws://localhost:9999/chatServer");
		return "chat";
	}

}
