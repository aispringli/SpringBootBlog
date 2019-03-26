package com.f5tv.springbootblog.controller.email;

import com.f5tv.springbootblog.config.email.EmailResultBean;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.service.email.EmailClientService;
import com.f5tv.springbootblog.tools.CheckTool;
import feign.QueryMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SpringLee
 * @Title: EmailController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:37 2019/3/22
 */
@RestController
@RequestMapping("Email")
public class EmailController {

    //注入结果 bean
    @Autowired
    EmailResultBean emailResultBean;
    //注入email 发送service
    @Autowired
    EmailClientService emailClientService;

    /**
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 检查邮箱地址
     * @Date 2019/3/22 15:46
     * @Param * @param emailAddress 待检查的邮箱地址
     **/
    @RequestMapping("/CheckEmailAddress")
    public ResponseResult CheckEmailAddress(String emailAddress) {
        //邮箱的判断
        if (StringUtils.isEmpty(emailAddress)) return emailResultBean.checkEmailAddressResult().get(101);
        if (CheckTool.checkEmailAddress(emailAddress)) {
            if (emailAddress.length() > 100) return emailResultBean.checkEmailAddressResult().get(103);
        }
        return emailResultBean.checkEmailAddressResult().get(0);
    }

    /**
     * @param subject 主题
     * @param message 消息内容
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 发送邮箱验证码，要包含 emailValidateCode被替换橙六位验证码
     * @Date 2019/3/22 15:47
     * @Param * @param emailAddress 邮箱地址
     **/
    @RequestMapping("/SendEmailValidateCode")
    public ResponseResult SendEmailValidateCode(String emailAddress, String subject, String message, int emailValidateCode) {
        try {
            String[] emailAddressArray = new String[1];
            emailAddressArray[0] = emailAddress;
            if (emailClientService.sendMailText(emailAddressArray, subject, message.replace("{emailValidateCode}", String.valueOf(emailValidateCode)), null))
                return emailResultBean.sendEmailResult().get(0);
            else return emailResultBean.sendEmailResult().get(101);
        } catch (Exception ex) {
            return emailResultBean.sendEmailResult().get(101);
        }
    }


    /**
     * @param emailValidateCode
     * @param rightEmailAddress
     * @param rightEmailValidateCode
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO
     * @Date 2019/3/25 10:14
     * @Param * @param emailAddress
     **/
    @RequestMapping("/CheckEmailValidateCode")
    public ResponseResult CheckEmailValidateCode(String emailAddress, String emailValidateCode, String rightEmailAddress, String rightEmailValidateCode) {
        if (StringUtils.isEmpty(emailAddress)) return emailResultBean.checkEmailValidateCodeResult().get(105);
        if (StringUtils.isEmpty(emailValidateCode)) return emailResultBean.checkEmailValidateCodeResult().get(101);
        if (emailValidateCode.length() != 6) return emailResultBean.checkEmailValidateCodeResult().get(102);


        if (StringUtils.isEmpty(rightEmailValidateCode)) return emailResultBean.checkEmailValidateCodeResult().get(103);
        if (rightEmailValidateCode.equals(emailValidateCode) && rightEmailAddress.equals(emailAddress))
            return emailResultBean.checkEmailValidateCodeResult().get(0);
        else return emailResultBean.checkEmailValidateCodeResult().get(104);
    }

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
    @RequestMapping("/SendEmailText")
    public ResponseResult SendEmailText(String[] recipients, String subject, String message, String[] attachments) {
        if (emailClientService.sendMailText(recipients, subject, message, attachments))
            return emailResultBean.sendEmailResult().get(0);
        else return emailResultBean.sendEmailResult().get(101);
    }

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
    @RequestMapping(value = "/SendEmailHtml",method = RequestMethod.POST)
    public ResponseResult SendEmailHtml(@RequestParam("recipients") String[] recipients, @RequestParam("subject") String subject,
                                        @RequestParam("templateName") String templateName,@RequestBody Map<String, Object> datas, @RequestParam(value = "attachments",required = false) String[] attachments) {
        if (emailClientService.sendMailHtml(recipients, subject, templateName, datas, attachments))
            return emailResultBean.sendEmailResult().get(0);
        else return emailResultBean.sendEmailResult().get(101);
    }

}
