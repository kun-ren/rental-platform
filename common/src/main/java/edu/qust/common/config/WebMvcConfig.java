package edu.qust.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Configure Thymeleaf and JSON view resolution
 * <ul>
 * <li>Use the URL for the Thymeleaf view</li>
 * <li>Append .json to the URL for the JSON view</li>
 * </ul>
 *
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.TEXT_HTML);
	}

	/**
	 * Map static-resource paths
	 *
	 * @param registry registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//Equivalent to <mvc:resources mapping="/**" location="/static/" />; multiple paths are supported
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/");
	}


	/**
	 * Configure ContentNegotiatingViewResolver
	 *
	 * @param manager manager
	 * @return org.springframework.web.servlet.ViewResolver
	 */
	@Bean
	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setContentNegotiationManager(manager);
		// Define all view resolvers
		List<ViewResolver> resolvers = new ArrayList<>();
		resolvers.add(jsonViewResolver());
		resolvers.add(thymeleafViewResolver());
		resolver.setViewResolvers(resolvers);
		return resolver;
	}

	/**
	 * Configure View (url+'.json' view) resolver to provide JSON output using jackson library to
	 * convert object in JSON format.
	 *
	 * @return org.springframework.web.servlet.ViewResolver
	 */
	@Bean
	public ViewResolver jsonViewResolver() {
		MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
		// Configure serialization for LocalDateTime and related types
		mappingJackson2JsonView.setObjectMapper(objectMapper());
		return (viewName, locale) -> mappingJackson2JsonView;
	}

	/**
	 * Configure the Thymeleaf view resolver
	 *
	 * @return org.thymeleaf.spring5.view.ThymeleafViewResolver
	 */
	private ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		// The order and viewNames settings are optional
		viewResolver.setOrder(1);
		viewResolver.setViewNames(new String[]{".html"});
		return viewResolver;
	}

	/**
	 * Create the template engine and inject the template resolver
	 *
	 * @return org.thymeleaf.spring5.SpringTemplateEngine
	 */
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	/**
	 * Create the template resolver
	 *
	 * @return org.thymeleaf.templateresolver.ITemplateResolver
	 */
	@Bean
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("classpath:/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	private ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		objectMapper.registerModule(javaTimeModule);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return objectMapper;
	}

	private MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		// Because the response Content-Type defaults to text/html, the default Jackson converter cannot parse it; see the Fastjson implementation
		jsonConverter.setSupportedMediaTypes(Collections.singletonList((MediaType.ALL)));
		// Configure serialization for LocalDateTime and related types
		jsonConverter.setObjectMapper(objectMapper());
		return jsonConverter;
	}

	/**
	 * Configure a custom JSON converter for @RestController, @ResponseBody, and @RequestBody
	 *
	 * @param converters converters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(0, customJackson2HttpMessageConverter());
	}
}
