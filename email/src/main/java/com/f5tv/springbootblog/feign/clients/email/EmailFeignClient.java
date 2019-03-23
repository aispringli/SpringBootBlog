package com.f5tv.springbootblog.feign.clients.email;

import com.f5tv.springbootblog.entity.core.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author SpringLee
 * @Title: EmailFeignClient
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:33 2019/3/22
 */
@FeignClient(name = "EmailFeignClient", url = "/")
public interface EmailFeignClient {

    /**
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 检查邮箱地址
     * @Date 2019/3/22 16:02
     * @Param * @param emailAddress 待检查的邮箱地址
     **/
    @RequestMapping(
            value = "/email/checkEmailAddress",
            method = RequestMethod.GET
    )
    ResponseResult checkEmailAddress(@RequestParam("emailAddress") String emailAddress);

    /**
     * @param subject 主题
     * @param message 消息内容
     * @param request
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 发送邮箱验证码，要包含 emailValidateCode被替换橙六位验证码
     * @Date 2019/3/22 15:47
     * @Param * @param emailAddress 邮箱地址
     **/
    @RequestMapping("/email/sendEmailValidateCode")
    ResponseResult sendEmailValidateCode(@RequestParam("emailAddress") String emailAddress, @RequestParam("subject") String subject,
                                         @RequestParam("message") String message, HttpServletRequest request);

    /**
     * @param request
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 检查邮箱验证码是否正确
     * @Date 2019/3/22 15:48
     * @Param * @param emailValidateCode 待检查验证码
     **/
    @RequestMapping("/email/checkEmailValidateCode")
    ResponseResult checkEmailValidateCode(@RequestParam("emailAddress") String emailAddress,
                                          @RequestParam("emailValidateCode") String emailValidateCode, HttpServletRequest request);

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
    @RequestMapping("/email/sendEmailText")
    ResponseResult sendEmailText(@RequestParam("recipients") String[] recipients, @RequestParam("subject") String subject,
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
    @RequestMapping("/email/sendEmailHtml")
    ResponseResult sendEmailHtml(@RequestParam("recipients") String[] recipients, @RequestParam("subject") String subject,
                                 @RequestParam("templateName") String templateName, @RequestParam("datas") Map<String, Object> datas, @RequestParam("attachments") String[] attachments);


}
