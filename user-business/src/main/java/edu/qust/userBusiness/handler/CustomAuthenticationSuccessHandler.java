package edu.qust.userBusiness.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.qust.common.base.Response;
import edu.qust.common.base.WebConstant;
import edu.qust.common.enums.RoleEnum;
import edu.qust.userBusiness.common.ClientType;
import edu.qust.userDao.entity.ProductRating;
import edu.qust.userService.MenuService;
import edu.qust.userService.ProductRatingService;
import edu.qust.userService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证成功处理器
 *
 */
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private ProductRatingService productRatingService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		Set<Integer> roleIdSet = new HashSet<>();
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			roleIdSet.add(RoleEnum.getByName(authority.getAuthority().substring(5)).getId());
		}

		BufferedReader reader = request.getReader();
		StringBuilder builder = new StringBuilder();
		String line = reader.readLine();
		while(line != null){
			builder.append(line);
			line = reader.readLine();
		}
		reader.close();
		ClientType clientType = JSON.parseObject(builder.toString(), ClientType.class);
		if (clientType != null){
			if("vue".equals(clientType.getName())){
				response.setContentType("application/json;charset=utf-8");
				PrintWriter writer = response.getWriter();
				Response responseData = Response.SUCCESS;
				writer.write(JSON.toJSONString(responseData));
				writer.flush();
				writer.close();
				request.getSession().setAttribute(WebConstant.Session.CURRENT_USER_ID_SESSION_KEY,
						userService.getUserIdByName(authentication.getName()));
				return;
			}
		}
		// 将用户菜单保存到session
		request.getSession().setAttribute(WebConstant.Session.MENU_VO_LIST_SESSION_KEY, menuService.listMenuVO(roleIdSet));
		request.getSession().setAttribute(WebConstant.Session.CURRENT_USER_ID_SESSION_KEY,
				userService.getUserIdByName(authentication.getName()));
		response.sendRedirect("/");

		Integer userId = userService.getUserIdByName(authentication.getName());
		LambdaQueryWrapper<ProductRating> s = new LambdaQueryWrapper<>();
		s.eq(ProductRating::getUserId,userId);
		s.orderByDesc(ProductRating::getTime);
		List<ProductRating> productList = productRatingService.list(s);
		if(productList.size() > 0 ) {
			List<String> products = productList.stream().limit(5).map(e -> {
				String p = e.getProductId() + ":" + e.getScore();
				return p;
			}).collect(Collectors.toList());
			redisTemplate.delete("user:" + userId);
			redisTemplate.opsForList().leftPushAll("user:" + userId, products);
		}
	}

}
