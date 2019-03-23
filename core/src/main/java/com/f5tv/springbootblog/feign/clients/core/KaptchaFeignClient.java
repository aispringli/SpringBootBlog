package com.f5tv.springbootblog.feign.clients.core;

import com.f5tv.springbootblog.entity.core.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

/**
 * @author SpringLee
 * @Title: KaptchaFeignClient
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:08 2019/3/22
 */
@FeignClient(name = "KaptchaFeignClient", url = "/")
//@Component(value = "KaptchaFeignClient")
public interface KaptchaFeignClient {

    @RequestMapping(
            value = "/VerificationCode/checkValidateCode",
            method = RequestMethod.GET
    )
    ResponseResult checkValidateCode(@RequestParam("validateCode") String validateCode, HttpServletRequest request);
}
