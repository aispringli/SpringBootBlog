package com.f5tv.springbootblog.controller.core;

import com.f5tv.springbootblog.config.beans.CommonResultBean;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @author 34499
 * @Title: KaptchaController
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 9:54 2019/3/16
 */
@RequestMapping("/VerificationCode")
@Controller
public class KaptchaController {

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private DefaultKaptcha captchaProducer;

    /**
     * @Author 34499
     * @Description //获取验证码 的 请求路径
     * @Date  10:01
     * @Param [httpServletRequest, httpServletResponse]
     * @return void
     **/
    @RequestMapping("/GetImage")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        try{
            byte[] captchaChallengeAsJpeg = null;
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            try {
                //生产验证码字符串并保存到session中
                String createText = captchaProducer.createText();
                httpServletRequest.getSession().setAttribute("validateCode", createText);
                //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
                BufferedImage challenge = captchaProducer.createImage(createText);
                ImageIO.write(challenge, "jpg", jpegOutputStream);
            } catch (IllegalArgumentException e) {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
            captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.setDateHeader("Expires", 0);
            httpServletResponse.setContentType("image/jpeg");
            ServletOutputStream responseOutputStream =
                    httpServletResponse.getOutputStream();
            responseOutputStream.write(captchaChallengeAsJpeg);
            responseOutputStream.flush();
            responseOutputStream.close();
        }catch (Exception ex){
            logger.error("验证码生成错误");
            ex.printStackTrace();
        }
    }

    @Autowired
    CommonResultBean commonResultBean;
    @RequestMapping("checkValidateCode")
    @ResponseBody
    public ResponseResult checkValidateCode(String validateCode, HttpServletRequest request){
        //验证码部分的判断
        String rightValidateCode = (String) request.getSession().getAttribute("validateCode");
        request.getSession().setAttribute("validateCode", "");
        if (StringUtils.isEmpty(validateCode)) return commonResultBean.pubicValidateCodeResult().get(101);
        if (validateCode.length() != 4) return commonResultBean.pubicValidateCodeResult().get(103);
        if (StringUtils.isEmpty(rightValidateCode)) return commonResultBean.pubicValidateCodeResult().get(102);
        if (!validateCode.toLowerCase().equals(rightValidateCode.toLowerCase()))
            return commonResultBean.pubicValidateCodeResult().get(104);
        return commonResultBean.pubicValidateCodeResult().get(0);
    }


}
