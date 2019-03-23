package com.f5tv.springbootblog.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @author SpringLee
 * @Title: EmailService
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 11:30 2019/3/21
 */
@Service
public class EmailClientService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final boolean ISHTML = true;
    private static final boolean ISMULTIPART = true;
    private static final String encoding = "UTF-8";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * @param subject     主题
     * @param message     内容
     * @param attachments 附件文件
     * @return boolean 是否发送成功
     * @Author SpringLee
     * @Description //TODO
     * @Date 2019/3/22 11:51
     * @Param * @param recipients 收件人
     **/
    public boolean sendMailText(String[] recipients, String subject, String message, String[] attachments) {
        try {
            MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, ISMULTIPART, encoding);
                composeMessageHeader(recipients, subject, attachments, messageHelper);
                messageHelper.setText(message, !ISHTML);
            };
            mailSender.send(mimeMessagePreparator);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * @param subject      主题
     * @param templateName 模板名称
     * @param datas        参数 map 键值对
     * @param attachments  附件
     * @return boolean 是否发送成功
     * @Author SpringLee
     * @Description //TODO
     * @Date 2019/3/22 11:52
     * @Param * @param recipients 收件人
     **/
    public boolean sendMailHtml(String[] recipients, String subject, String templateName, Map<String, Object> datas, String[] attachments) {
        try {
            MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, ISMULTIPART, encoding);
                composeMessageHeader(recipients, subject, attachments, messageHelper);
                messageHelper.setText(buildMessage(templateName, datas), ISHTML);
            };
            mailSender.send(mimeMessagePreparator);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return false;
        }
        return true;
    }

    private void composeMessageHeader(String[] recipients, String subject, String[] attachments,
                                      MimeMessageHelper messageHelper) throws MessagingException {
        messageHelper.setFrom(from);
        messageHelper.setTo(recipients);
        messageHelper.setSubject(subject);
        if (attachments != null) {
            for (String filename : attachments) {
                FileSystemResource file = new FileSystemResource(filename);
                messageHelper.addAttachment(file.getFilename(), file);
            }
        }
    }

    public String buildMessage(String templateName, Map<String, Object> datas) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : datas.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return templateEngine.process(templateName, context);
    }
}
