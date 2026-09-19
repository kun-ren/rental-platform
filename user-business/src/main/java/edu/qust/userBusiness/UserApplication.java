package edu.qust.userBusiness;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import edu.qust.common.util.ApplicationContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/*@SpringBootApplication(exclude = SecurityAutoConfiguration.class,
        scanBasePackages = {"edu.qust.userBusiness","edu.qust.userService","edu.qust.userDao", "edu.qust.common" })*/
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@ComponentScan(basePackages = {"edu.qust.*"})
@EnableMPP
public class UserApplication   {
    public static void main(String[] args){
        ApplicationContext applicationContext = SpringApplication.run(UserApplication.class,args);
        ApplicationContextUtil.setApplicationContext(applicationContext);
    }
}
