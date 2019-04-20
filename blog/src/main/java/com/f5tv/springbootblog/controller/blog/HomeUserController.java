package com.f5tv.springbootblog.controller.blog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SpringLee
 * @Title: HomeUserController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 17:48 2019/4/14
 */
@RequestMapping("HomeUser")
@Controller
public class HomeUserController {

    @RequestMapping("Personal")
    public String Personal(){
        return "HomeUser/Personal";
    }

    @RequestMapping("UpdateLogoImg")
    public String UpdateLogoImg(){
        return "HomeUser/UpdateLogoImg";
    }

    @RequestMapping("UpdateMotto")
    public String UpdateMotto(){
        return "HomeUser/UpdateMotto";
    }
    @RequestMapping("UpdatePassword")
    public String UpdatePassword(){
        return "HomeUser/UpdatePassword";
    }

    @RequestMapping("UpdateEmail")
    public String UpdateEmail(){
        return "HomeUser/UpdateEmail";
    }



    //找回密码
    @RequestMapping("RetrievePasswordEmail")
    public String RetrievePasswordEmail() {

        return "/User/RetrievePasswordEmail";
    }
}
