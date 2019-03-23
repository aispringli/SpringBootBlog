package com.f5tv.springbootblog.config.beans;

import com.f5tv.springbootblog.entity.core.ResponseResult;
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
@Component(value = "CommonResultBean")
public class CommonResultBean {

    //private Logger logger= LoggerFactory.getLogger(getClass());
    //通用错误结果
    @Bean
    public Map<Integer, ResponseResult> publicErrorResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(-101,new ResponseResult(-101,true,"服务器发生了异常，请联系管理员解决"));
        map.put(-102,new ResponseResult(-102,true,"客官请稍等，服务器开了小差"));
        //logger.info("bean: publicErrorResult 加载完成");
        return map;
    }

    //验证码部分的
    @Bean
    public Map<Integer, ResponseResult> pubicValidateCodeResult(){
        Map<Integer,ResponseResult> map=new HashMap<>();
        map.put(101,new ResponseResult(101,"没有输入验证码"));
        map.put(102,new ResponseResult(102,"没有生成验证码，非法访问"));
        map.put(103,new ResponseResult(103,"请输入正确的四位验证码"));
        map.put(104,new ResponseResult(104,"验证码不正确"));
        map.put(0,new ResponseResult(0,true,"验证码正确"));
        return map;
    }

}
