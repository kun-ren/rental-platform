package edu.qust.userBusiness.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * elasticSearch config
 *
 */
@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = {"edu.qust.userDao.repository"})
public class ElasticSearchConfig implements InitializingBean {
	private static final String KEY = "es.set.netty.runtime.available.processors";

	static {
		System.setProperty(KEY, "false");
	}

	@Override
	public void afterPropertiesSet() {
		log.info("{}:{}", KEY, System.getProperty(KEY));
	}

}
