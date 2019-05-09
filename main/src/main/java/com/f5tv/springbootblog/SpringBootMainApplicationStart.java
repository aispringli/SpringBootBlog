package com.f5tv.springbootblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 34499
 * @Title: SpringBootMainApplicationStart
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 15:41 2019/3/16
 */
//@SpringBootApplication
// 开启SpringCache缓存支持
@EnableCaching
// 微服务调用feign
@EnableFeignClients
@EnableTransactionManagement
@SpringBootApplication()

public class SpringBootMainApplicationStart {
    public static void main(String[] args){
        SpringApplication.run(SpringBootMainApplicationStart.class,args);
    }

}
