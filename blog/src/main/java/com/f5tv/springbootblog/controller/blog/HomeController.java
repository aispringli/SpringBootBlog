package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.BlogService;
import com.f5tv.springbootblog.service.blog.CategoryService;
import com.github.pagehelper.PageHelper;
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
@RequestMapping(value = {"Home","/"})
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    BlogService blogService;

    @RequestMapping(value = {"Index","/"})
    public ModelAndView Index(Integer page){

        ModelAndView modelAndView=new ModelAndView("/Home/Index");
        if (page == null || page < 1) page = 1;
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setBlogStatus(0);
        blogEntity.setCategoryId(0);
        blogEntity.setUserId(0);
        PageHelper.startPage(page, 10);
        modelAndView.addObject("blogLists",blogService.selectBlogAll(blogEntity));
//        modelAndView.addObject("pageNum",blogService.selectBlogAllNum(blogEntity));
        return modelAndView;
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
