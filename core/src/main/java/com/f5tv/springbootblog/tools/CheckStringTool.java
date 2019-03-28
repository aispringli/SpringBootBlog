package com.f5tv.springbootblog.tools;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author SpringLee
 * @Title: CheckTool
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:59 2019/3/19
 */
@Configuration
@Component(value = "CheckStringTool")
public class CheckStringTool {

    /**
     * @return boolean 是否是邮箱地址
     * @Author SpringLee
     * @Description 验证邮箱地址是否格式是否正确
     * @Date 2019/3/19 21:09
     * @Param * @param emailAddress 邮箱地址
     **/
    public  boolean CheckEmailAddress(String emailAddress) {
        String Single_Email_Regex = "^(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})+$";
        if (StringUtils.isEmpty(emailAddress)) return false;
        return emailAddress.matches(Single_Email_Regex);
    }

    public boolean CheckStringHazChinese(String string) {
        if (StringUtils.isEmpty(string)) return false;
        String String_Chinese_Regex = "^[\u4e00-\u9fa5]+$";
        return string.matches(String_Chinese_Regex);
    }

    public static boolean CheckStringHasSpecialChar(String string) {
        if (StringUtils.isEmpty(string)) return false;
        String Single_String_Regex = "^[\u4e00-\u9fa5a-zA-Z0-9!%$#,.:;]+$";
        return !string.matches(Single_String_Regex);
    }

    public boolean CheckStringLength(String string, int minLenth, int maxLenth) {
        if (StringUtils.isEmpty(string)) return false;
        return string.length() >= minLenth && string.length() <= maxLenth;
    }

    public boolean CheckStringComplexity(String string, int grade) {
        int flag = -1;
        //复杂（同时包含数字，字母，特殊符号）
        String String_Complex_Regex = "^^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*_-]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*_-]+$)(?![\\d!@#$%^&*_-]+$)[a-zA-Z\\d!@#$%^&*_-]+$";
        //中级（包含字母和数字）
        String String_Medium_Regex = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";
        //简单（只包含数字或字母）
        String String_Simple_Regex = "^(?:\\d+|[a-zA-Z]+|[!@#$%^&*]+)$";
        if (!StringUtils.isEmpty(string)) {
            if (string.matches(String_Complex_Regex)) flag = 3;
            else if (string.matches(String_Medium_Regex)) flag = 2;
            else if (string.matches(String_Simple_Regex)) flag = 1;
        }
        return flag >= grade;
    }

}
