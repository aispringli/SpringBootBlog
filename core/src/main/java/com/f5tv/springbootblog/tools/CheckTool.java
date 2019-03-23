package com.f5tv.springbootblog.tools;

/**
 * @author SpringLee
 * @Title: CheckTool
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:59 2019/3/19
 */
public class CheckTool {


    private static final String Single_Email_Regex="^(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})+$";

    private static final String Single_String_Regex="^[\u4e00-\u9fa5a-zA-Z0-9!%$#,.]+$";

    /**
     * @Author SpringLee
     * @Description 验证邮箱地址是否格式是否正确
     * @Date 2019/3/19 21:09
     * @Param  * @param emailAddress 邮箱地址
     * @return boolean 是否是邮箱地址
     **/
    public static boolean checkEmailAddress(String emailAddress)
    {
        if(emailAddress==null||"".equals(emailAddress))return false;
         return emailAddress.matches(Single_Email_Regex);
    }

    public static boolean checkStringHasSpecialChar(String string)
    {
        if(string==null||"".equals(string))return false;
        return !string.matches(Single_String_Regex);
    }

}
