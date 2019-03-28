package com.f5tv.springbootblog.config.user.configbeans;

import com.f5tv.springbootblog.entity.core.ResponseResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 34499
 * @Title: UserResultBean
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 11:08 2019/3/12
 */
@Configuration
@Component(value = "UserResultBean")
public class UserResultBean {
    //用户注册结果
    @Bean
    public Map<Integer, ResponseResult> userRegisterResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(0,new ResponseResult(0,true,"注册成功"));

        map.put(201,new ResponseResult(201,"注册失败，用户名长度请在6-20之间"));
        map.put(202,new ResponseResult(202,"注册失败，用户名不允许有空格等其他字符"));
        map.put(203,new ResponseResult(203,"注册失败，用户名已被注册使用"));

        map.put(301,new ResponseResult(301,"注册失败，请输入合法的邮箱地址"));
        map.put(302,new ResponseResult(302,"注册失败，邮箱地址最大100字符长度"));
        map.put(303,new ResponseResult(303,"验证邮件发送失败，请检查邮箱是否可用"));
        map.put(304,new ResponseResult(304,"注册失败，邮箱已被注册使用"));
        map.put(305,new ResponseResult(305,"注册失败，邮箱验证码不正确"));

        map.put(401,new ResponseResult(401,"密码长度请在6-20之间"));
        map.put(402,new ResponseResult(402,"密码不允许有空格等其他字符"));
        map.put(403,new ResponseResult(403,"密码简单，请输入复杂的密码"));

        map.put(501,new ResponseResult(501,"用户名已被注册，请换一个"));
        map.put(502,new ResponseResult(502,"邮箱已被注册，如忘记密码可选择重置密码"));
        return map;
    }

    //用户登陆结果
    @Bean
    public Map<Integer, ResponseResult> userLoginResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(0,new ResponseResult(0,true,"登陆成功"));

        map.put(201,new ResponseResult(201,"登陆失败，用户名长度请在6-20之间"));
        map.put(202,new ResponseResult(202,"登陆失败，用户名不允许有空格等其他字符"));
        map.put(203,new ResponseResult(203,"登陆失败，用户不存在"));

        map.put(301,new ResponseResult(301,"登陆失败，请输入合法的邮箱地址"));
        map.put(302,new ResponseResult(302,"登陆失败，邮箱地址最大100字符长度"));
        map.put(303,new ResponseResult(334,"登陆失败，邮箱未注册"));

        map.put(401,new ResponseResult(401,"登陆失败，密码长度请在6-20之间"));
        map.put(402,new ResponseResult(402,"登陆失败，密码不允许有空格等其他字符"));
        map.put(403,new ResponseResult(403,"登陆失败，密码不对"));

        map.put(501,new ResponseResult(501,"账户被禁用，请联系管理员解决"));
        return map;
    }


    //用户重置密码结果
    @Bean
    public Map<Integer, ResponseResult> userResetPasswordResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(0,new ResponseResult(0,true,"修改成功"));
        map.put(101,new ResponseResult(101,"邮箱未注册"));
        map.put(201,new ResponseResult(201,"修改失败"));
        map.put(202,new ResponseResult(202,"非法访问，校验码格式不正确"));
        map.put(203,new ResponseResult(203,"修改失败，校验码不正确，可以重新重置密码试试"));

        return map;
    }
}
