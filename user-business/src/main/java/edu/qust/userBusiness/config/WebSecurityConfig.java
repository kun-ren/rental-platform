package edu.qust.userBusiness.config;

import edu.qust.userBusiness.common.WebConstant;
import edu.qust.userBusiness.filter.CaptchaValidationFilter;
import edu.qust.userDao.dto.RoleResourceDTO;
import edu.qust.userDao.dto.UserRoleDTO;
import edu.qust.userDao.mapper.UserRoleMapper;
import edu.qust.userService.RoleResourceService;
import edu.qust.userService.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import edu.qust.common.base.Constant;

import javax.annotation.Resource;
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
	@Resource
	private UserRoleMapper userRoleMapper;
	@Resource
	private RoleResourceService roleResourceService;
	@Value("${userBusiness.security.static-asset-path}")
	private String[] staticAssetPath;
	@Resource
	private CaptchaValidationFilter captchaValidationFilter;
	@Autowired
	//@Qualifier("loginSuccessHandlerImpl")
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(staticAssetPath)
				.antMatchers("/chatServer/**")
				.regexMatchers(HttpMethod.GET, "/register", "/forget", "/captcha", "/captcha/error", "/items/pay/return(.+)")
				.regexMatchers(HttpMethod.POST, "/users", "/emails/(.+)/send-captcha", "/users/(.+)/check-exists");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.cors().and().csrf().disable();
		//http.formLogin().permitAll().loginProcessingUrl("/login").successForwardUrl("/login/success");
		// Add the CAPTCHA validation filter before the username/password authentication filter
		http.addFilterBefore(captchaValidationFilter, UsernamePasswordAuthenticationFilter.class);
		List<RoleResourceDTO> roleResourceDTOList = roleResourceService.listRoleResourceDTOPlusRoot();
		log.info("RoleResourceDTO list: {}", roleResourceDTOList);
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
		for (RoleResourceDTO dto : roleResourceDTOList) {
			registry.regexMatchers(HttpMethod.resolve(dto.getResourceMethod()), dto.getResourceURL())
					.hasAnyRole(dto.getRoleIdentifierConcat().split(Constant.Separator.COMMA));
		}
		registry.regexMatchers(HttpMethod.GET, "/stuffs/vue/in","/items/vue/in","/stuffs/vue/out").hasAnyRole("LESSOR,LESSEE,ROOT,GUEST");

		registry.anyRequest().hasRole(WebConstant.RoleIdentifier.ROOT)
				.and().formLogin().successHandler(authenticationSuccessHandler).loginPage("/login").permitAll()
				.and().logout().logoutUrl(AuthUtil.LOGOUT_URL).logoutSuccessUrl("/login")
				.deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inMemoryUserDetailsManager());
	}

	//Load users into memory
	//TODO: loaderUserByUserName
	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		List<UserRoleDTO> userRoleDTOList = userRoleMapper.listUserRoleDTO();
		log.info("userRoleDTO list: {}", userRoleDTOList.toString());
		List<UserDetails> userDetailsList = new ArrayList<>();
		for (UserRoleDTO dto : userRoleDTOList) {
			userDetailsList.add(
					User.withUsername(dto.getUsername())
					.password(dto.getPassword())
					.roles(dto.getRoleIdentifierConcat().split(Constant.Separator.COMMA)).build()
			);
		}
		return new InMemoryUserDetailsManager(userDetailsList);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
