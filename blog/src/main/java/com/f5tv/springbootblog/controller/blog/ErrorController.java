package com.f5tv.springbootblog.controller.blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SpringLee
 * @Title: ErrorController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 17:56 2019/4/26
 */
@RequestMapping("/error")
@Controller
public class ErrorController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("400")
    public String Error400(HttpServletRequest request){
        return "Error/400";
    }

    @RequestMapping("404")
    public String Error404(HttpServletRequest request){
        return "Error/404";
    }
    @RequestMapping("500")
    public String Error500(HttpServletRequest request){
        return "Error/500";
    }
}
