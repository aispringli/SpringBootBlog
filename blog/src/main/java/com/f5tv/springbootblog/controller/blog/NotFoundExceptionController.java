package com.f5tv.springbootblog.controller.blog;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SpringLee
 * @Title: NotFoundExceptionController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:58 2019/5/20
 */
@Controller
public class NotFoundExceptionController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = {"/error"})
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView modelAndView=new ModelAndView("Error/404");
        modelAndView.addObject("msg","您访问的内容不存在或已被永久性的删除了");
        return modelAndView;
    }
}
