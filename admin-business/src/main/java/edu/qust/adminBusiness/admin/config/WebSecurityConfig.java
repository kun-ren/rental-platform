package edu.qust.adminBusiness.admin.config;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.common.base.Constant;
import edu.qust.common.base.WebConstant;
import edu.qust.commonInterface.UserRoleServiceApi;
import edu.qust.commonInterface.dto.UserRoleApiDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringSecurity config
 *
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Reference
	UserRoleServiceApi userRoleServiceApi;

	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/plugins/**", "/css/**", "/js/**","/favicon.ico");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.cors().and().csrf().disable();
		http.formLogin().loginPage("/login").successHandler(authenticationSuccessHandler);
		http.authorizeRequests().antMatchers("/categories/**","/users","/items/**","/chat/**").hasRole("ROOT");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inMemoryUserDetailsManager()).passwordEncoder(new BCryptPasswordEncoder());
	}


	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		log.info("password:");
		log.info(new BCryptPasswordEncoder().encode("password"));
		List<UserRoleApiDTO> userRoleApiDTOList = userRoleServiceApi.listUserRole();
		log.info("userRoleDTO list: {}", userRoleApiDTOList.toString());
		List<UserDetails> userDetailsList = new ArrayList<>();
		for (UserRoleApiDTO dto : userRoleApiDTOList) {
			userDetailsList.add(
					User.withUsername(dto.getUsername())
							.password(dto.getPassword())
							.roles(dto.getRoleIdentifierConcat().split(Constant.Separator.COMMA)).build()
			);
		}
		return new InMemoryUserDetailsManager(userDetailsList);
	}
}
