package com.f5tv.springbootblog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 34499
 * @Title: SpringBootMainApplicationStart
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 15:41 2019/3/16
 */
@SpringBootApplication
// 开启SpringCache缓存支持
@EnableCaching
// 微服务调用feign
@EnableFeignClients
public class SpringBootMainApplicationStart {
    public static void main(String[] args){
        SpringApplication.run(SpringBootMainApplicationStart.class,args);
    }
}
