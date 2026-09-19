package edu.qust.adminBusiness.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"edu.qust.adminBusiness.admin",
		"edu.qust.adminDao","edu.qust.adminService","edu.qust.common"})
@EnableAsync
public class adminApplication {
	public static void main(String[] args) {
		SpringApplication.run(adminApplication.class, args);
	}
}
