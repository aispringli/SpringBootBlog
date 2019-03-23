package com.f5tv.springbootblog.controller.email;

import com.f5tv.springbootblog.config.email.EmailResultBean;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.service.email.EmailClientService;
import com.f5tv.springbootblog.tools.CheckTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author SpringLee
 * @Title: EmailController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:37 2019/3/22
 */
@RestController
@RequestMapping("email")
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
    @RequestMapping("checkEmailAddress")
    public ResponseResult checkEmailAddress(String emailAddress) {
        //邮箱的判断
        if (StringUtils.isEmpty(emailAddress)) return emailResultBean.checkEmailAddressResult().get(101);
        if (CheckTool.checkEmailAddress(emailAddress)) {
            if (emailAddress.length() > 100) return emailResultBean.checkEmailAddressResult().get(103);
        }
        return emailResultBean.checkEmailAddressResult().get(102);
    }

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
    @RequestMapping("sendEmailValidateCode")
    public ResponseResult sendEmailValidateCode(String emailAddress, String subject, String message, HttpServletRequest request) {
        try {
            String[] emailAddressArray = new String[1];
            emailAddressArray[0] = emailAddress;
            int emailValidateCode = (int) ((Math.random() * 9 + 1) * 100000);
            request.getSession().setAttribute("emailValidateCode", emailValidateCode);
            request.getSession().setAttribute("emailAddress", emailAddress);
            if (emailClientService.sendMailText(emailAddressArray, subject, message.replace("emailValidateCode", String.valueOf(emailValidateCode)), null))
                return emailResultBean.sendEmailResult().get(0);
            else return emailResultBean.sendEmailResult().get(101);
        } catch (Exception ex) {
            return emailResultBean.sendEmailResult().get(101);
        }
    }

    /**
     * @param request
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 检查邮箱验证码是否正确
     * @Date 2019/3/22 15:48
     * @Param * @param emailValidateCode 待检查验证码
     **/
    @RequestMapping("checkEmailValidateCode")
    public ResponseResult checkEmailValidateCode(String emailAddress, String emailValidateCode, HttpServletRequest request) {
        if (StringUtils.isEmpty(emailAddress)) return emailResultBean.checkEmailValidateCodeResult().get(105);
        if (StringUtils.isEmpty(emailValidateCode)) return emailResultBean.checkEmailValidateCodeResult().get(101);
        if (emailValidateCode.length() != 6) return emailResultBean.checkEmailValidateCodeResult().get(102);
        String rightEmailValidateCode = (String) request.getSession().getAttribute(emailValidateCode);
        String rightEmailAddress = (String) request.getSession().getAttribute(emailAddress);
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
    @RequestMapping("sendEmailText")
    public ResponseResult sendEmailText(String[] recipients, String subject, String message, String[] attachments) {
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
    @RequestMapping("sendEmailHtml")
    public ResponseResult sendEmailHtml(String[] recipients, String subject, String templateName, Map<String, Object> datas, String[] attachments) {
        if (emailClientService.sendMailHtml(recipients, subject, templateName, datas, attachments))
            return emailResultBean.sendEmailResult().get(0);
        else return emailResultBean.sendEmailResult().get(101);
    }

}
