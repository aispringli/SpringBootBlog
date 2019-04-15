package com.f5tv.springbootblog.controller.blog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("Index")
    public String Index(){
        return "/Home/Index";
    }

    @RequestMapping("Category")
    public String Category(){
        return "/Home/Category";
    }

    @RequestMapping("Personal")
    public String Personal(){
        return "/Home/Personal";
    }

    @RequestMapping("WriteBlog")
    public String WriteBlog(){
        return "/Home/WriteBlog";
    }
}
