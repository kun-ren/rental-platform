package edu.qust.adminBusiness.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus Config
 *
 */
@Configuration
@MapperScan("edu.qust.adminDao.mapper")
public class MybatisPlusConfig {
}
