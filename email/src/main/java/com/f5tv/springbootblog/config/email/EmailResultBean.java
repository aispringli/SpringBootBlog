package com.f5tv.springbootblog.config.email;

import com.f5tv.springbootblog.entity.core.ResponseResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SpringLee
 * @Title: EmailResultBean
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:48 2019/3/22
 */
@Configuration
@Component(value = "EmailResultBean")
public class EmailResultBean {

    @Bean
    public Map<Integer, ResponseResult> checkEmailAddressResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(101,new ResponseResult(101,"没有输入邮箱地址"));
        map.put(102,new ResponseResult(102,"邮箱地址不合法"));
        map.put(103,new ResponseResult(103,"邮箱地址最大100字符长度"));
        map.put(0,new ResponseResult(0,true,"邮箱地址合法"));
        return map;
    }

    @Bean
    public Map<Integer, ResponseResult> sendEmailValidateCodeResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();

        return map;
    }

    @Bean
    public Map<Integer, ResponseResult> checkEmailValidateCodeResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(101,new ResponseResult(101,"未输入邮箱验证码"));
        map.put(102,new ResponseResult(102,"输入邮箱验证码不是六位"));
        map.put(103,new ResponseResult(103,"未发送邮箱验证码，非法访问"));
        map.put(104,new ResponseResult(104,"输入邮箱验证码不正确"));
        map.put(105,new ResponseResult(105,"未输入邮箱地址"));
        map.put(0,new ResponseResult(0,true,"邮箱验证码正确"));
        return map;
    }

    @Bean
    public Map<Integer, ResponseResult> sendEmailResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(101,new ResponseResult(101,"发送失败,请检查邮箱地址"));
        map.put(0,new ResponseResult(0,true,"发送成功,请到邮箱查收"));
        return map;
    }
}
