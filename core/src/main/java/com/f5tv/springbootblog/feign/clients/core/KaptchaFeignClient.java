package com.f5tv.springbootblog.feign.clients.core;

import com.f5tv.springbootblog.entity.core.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author SpringLee
 * @Title: KaptchaFeignClient
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:08 2019/3/22
 */
//@FeignClient(name = "KaptchaFeignClient", url = "https://127.0.0.1")
@FeignClient(name = "KaptchaFeignClient", url = "https://f5tv.com")
//@Component(value = "KaptchaFeignClient")
public interface KaptchaFeignClient {

    @RequestMapping(
            value = "/VerificationCode/CheckValidateCode"
    )
    ResponseResult CheckValidateCode(@RequestParam("validateCode") String validateCode, @RequestParam("rightValidateCode") String rightValidateCode);
}
