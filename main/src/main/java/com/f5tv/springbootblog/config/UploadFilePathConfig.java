package com.f5tv.springbootblog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author SpringLee
 * @Title: UploadFilePathConfig
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 17:01 2019/5/8
 */
@Configuration
public class UploadFilePathConfig implements WebMvcConfigurer  {

    @Value("${file.imgFolder}")
    private String imgFolder;

    @Value("${file.staticAccessPath}")
    private String staticAccessPath;

//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setLocation(uploadFolder);
//        //文件最大
//        factory.setMaxFileSize("5MB");
//        // 设置总上传数据总大小
//        factory.setMaxRequestSize("10MB");
//        return factory.createMultipartConfig();
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 图片传路径 */
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + imgFolder);

    }
}
