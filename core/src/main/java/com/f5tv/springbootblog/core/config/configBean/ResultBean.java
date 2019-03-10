package com.f5tv.springbootblog.core.config.configBean;

import com.f5tv.springbootblog.core.entity.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 34499
 * @Title: ResultBean
 * @ProjectName SpringBootBlog
 * @Description: 存储操作结果集的beans
 * @date 12:38 2019/3/7
 */
@Configuration
@Component
public class ResultBean {

    //private Logger logger= LoggerFactory.getLogger(getClass());
    //通用错误结果
    @Bean
    public Map<Integer,ResponseResult> publicErrorResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(-101,new ResponseResult(-101,true,"服务器发生了异常，请联系管理员解决"));
        map.put(-102,new ResponseResult(-102,true,"客官请稍等，服务器开了小差"));
        //logger.info("bean: publicErrorResult 加载完成");
        return map;
    }

    //用户注册结果
    @Bean
    public Map<Integer, ResponseResult> userRegisterResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(0,new ResponseResult(0,true,"注册成功"));

        map.put(101,new ResponseResult(101,"注册失败，没有输入验证码"));
        map.put(102,new ResponseResult(102,"注册失败，验证码不正确"));

        map.put(201,new ResponseResult(201,"注册失败，用户名长度请在6-20之间"));
        map.put(202,new ResponseResult(202,"注册失败，用户名不允许有空格等其他字符"));
        map.put(203,new ResponseResult(203,"注册失败，用户名已被注册使用"));

        map.put(301,new ResponseResult(301,"注册失败，请输入合法的邮箱地址"));
        map.put(302,new ResponseResult(302,"注册失败，邮箱地址最大100字符长度"));
        map.put(303,new ResponseResult(303,"验证邮件发送失败，请检查邮箱是否可用"));
        map.put(304,new ResponseResult(304,"注册失败，邮箱已被注册使用"));
        map.put(305,new ResponseResult(305,"注册失败，邮箱验证码不正确"));

        map.put(401,new ResponseResult(401,"注册失败，密码长度请在6-20之间"));
        map.put(402,new ResponseResult(402,"注册失败，密码不允许有空格等其他字符"));
        map.put(403,new ResponseResult(403,"注册失败，密码简单，请输入复杂的密码"));

        //logger.info("bean: userRegisterResult 加载完成");
        return map;
    }

    //用户登陆结果
    @Bean
    public Map<Integer, ResponseResult> userLoginResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(0,new ResponseResult());



        return map;
    }

    //用户重置密码结果
    @Bean
    public Map<Integer, ResponseResult> userResetPasswordResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(0,new ResponseResult());



        return map;
    }



}
