package edu.qust.userBusiness.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * websocket config
 *
 */
@Configuration
public class WebSocketConfig {

	/**
	 * 用于扫描和注册所有携带ServerEndPoint注解的实例。
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
