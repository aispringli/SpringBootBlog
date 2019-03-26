package com.f5tv.springbootblog.feign.clients.email;

import com.f5tv.springbootblog.entity.core.ResponseResult;
import feign.Headers;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

/**
 * @author SpringLee
 * @Title: EmailFeignClient
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:33 2019/3/22
 */
@FeignClient(name = "EmailFeignClient", url = "127.0.0.1")
public interface EmailFeignClient {

    /**
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 检查邮箱地址
     * @Date 2019/3/22 16:02
     * @Param * @param emailAddress 待检查的邮箱地址
     **/
    @RequestMapping(
            value = "/Email/CheckEmailAddress",
            method = RequestMethod.GET
    )
    ResponseResult CheckEmailAddress(@RequestParam("emailAddress") String emailAddress);

    /**
     * @param subject           主题
     * @param message           消息内容
     * @param emailValidateCode 验证码
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description  发送邮箱验证码，要包含 emailValidateCode被替换成六位验证码
     * @Date 2019/3/25 10:09
     * @Param * @param emailAddress 邮箱地址
     **/
    @RequestMapping("/Email/SendEmailValidateCode")
    ResponseResult SendEmailValidateCode(@RequestParam("emailAddress") String emailAddress, @RequestParam("subject") String subject,
                                         @RequestParam("message") String message, @RequestParam("emailValidateCode") int emailValidateCode);

    /**
     * @param emailValidateCode 待检查邮箱验证码
     * @param rightEmailAddress 正确的邮箱地址
     * @param rightEmailValidateCode 正确邮箱验证码
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 检查邮箱验证码是否正确
     * @Date 2019/3/25 10:15
     * @Param * @param emailAddress 待检查邮箱地址
     **/
    @RequestMapping("/Email/CheckEmailValidateCode")
    ResponseResult CheckEmailValidateCode(@RequestParam("emailAddress") String emailAddress, @RequestParam("emailValidateCode") String emailValidateCode,
                                          @RequestParam("rightEmailAddress") String rightEmailAddress, @RequestParam("rightEmailValidateCode") String rightEmailValidateCode);

    /**
     * @param subject     主题
     * @param message     内容
     * @param attachments 附件
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 发送txt格式的邮件
     * @Date 2019/3/22 15:49
     * @Param * @param recipients 收件地址
     **/
    @RequestMapping("/Email/SendEmailText")
    ResponseResult SendEmailText(@RequestParam("recipients") String[] recipients, @RequestParam("subject") String subject,
                                 @RequestParam("message") String message, @RequestParam("attachments") String[] attachments);

    /**
     * @param subject      主题
     * @param templateName html模板名称
     * @param datas        数据-键值对
     * @param attachments  附件
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 发送html格式的邮件
     * @Date 2019/3/22 15:49
     * @Param * @param recipients 收件人
     **/
    @RequestMapping(value = "/Email/SendEmailHtml",method = RequestMethod.POST)
    //@Headers("Content-Type: application/json")
    ResponseResult SendEmailHtml(@RequestParam("recipients") String[] recipients, @RequestParam("subject") String subject,
                                 @RequestParam("templateName") String templateName, @QueryMap Map<String, Object> datas, @RequestParam("attachments") String[] attachments);

}
