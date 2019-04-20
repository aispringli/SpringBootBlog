package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author SpringLee
 * @Title: HomeController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 11:25 2019/4/12
 */
@Controller
@RequestMapping("Home")
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("Index")
    public String Index(){
        return "/Home/Index";
    }

    @RequestMapping("Category")
    public ModelAndView Category(){
        ModelAndView modelAndView=new ModelAndView("/Home/Category");
        long userId=((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        modelAndView.addObject("categoryLists",categoryService.categorySelectByUserId(userId));
        return modelAndView;
    }

    @RequestMapping("Personal")
    public String Personal(){
        return "/Home/Personal";
    }

    @RequestMapping("WriteBlog")
    public ModelAndView WriteBlog(){
        ModelAndView modelAndView=new ModelAndView("/Home/WriteBlog");
        long userId=((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        modelAndView.addObject("categoryLists",categoryService.categorySelectByUserId(userId));
        return modelAndView;
    }
}
