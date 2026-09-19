package edu.qust.userBusiness.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus Config
 *
 */
@Configuration
@MapperScan("edu.qust.userDao.mapper")
public class MybatisPlusConfig {
}
