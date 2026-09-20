package edu.qust.userBusiness.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
	public String index(Authentication authentication, HttpServletRequest request, Model model) throws UnknownHostException {
		model.addAttribute("username", authentication.getName());
		model.addAttribute("webSocketUrl",
				"ws://" + InetAddress.getLocalHost().getHostAddress() + ":" + request.getServerPort() + request.getContextPath() + "/chatServer");
		return "chat";
	}

}
