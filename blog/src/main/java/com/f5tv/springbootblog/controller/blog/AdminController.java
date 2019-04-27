package com.f5tv.springbootblog.controller.blog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author SpringLee
 * @Title: AdminController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 10:26 2019/4/26
 */
@Controller
@RequestMapping("Admin")
public class AdminController {

    @RequestMapping("Index")
    public ModelAndView Index(){
        ModelAndView modelAndView=new ModelAndView("/Admin/Index");
        return modelAndView;
    }

    @RequestMapping("UserManger")
    public ModelAndView UserManger(){
        ModelAndView modelAndView=new ModelAndView("/Admin/UserManger");
        return modelAndView;
    }

    @RequestMapping("CategoryManger")
    public ModelAndView CategoryManger(){
        ModelAndView modelAndView=new ModelAndView("/Admin/CategoryManger");
        return modelAndView;
    }

    @RequestMapping("BlogManger")
    public ModelAndView BlogManger(){
        ModelAndView modelAndView=new ModelAndView("/Admin/BlogManger");
        return modelAndView;
    }

    @RequestMapping("CommentManger")
    public ModelAndView CommentManger(){
        ModelAndView modelAndView=new ModelAndView("/Admin/CommentManger");
        return modelAndView;
    }
}
