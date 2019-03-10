package com.f5tv.springbootblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 34499
 * @Title: UserController
 * @ProjectName spring
 * @Description: TODO
 * @date 12:38 2019/3/5
 */
@Controller
@RequestMapping("/User")
public class UserController {

    @RequestMapping("/Logo")
    String UserLogo(){
        return "/User/Logo";
    }
}
