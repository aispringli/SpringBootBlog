package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.Category;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.BlogService;
import com.f5tv.springbootblog.service.blog.CategoryService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author SpringLee
 * @Title: BlogController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:23 2019/4/21
 */
@Controller
@RequestMapping("Blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("HandleBlogAdd")
    @ResponseBody
    public ResponseResult HandleBlogAdd(BlogEntity blogEntity) {
        long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return blogService.BlogInsert(blogEntity, userId);
    }

    @RequestMapping("MyBlog")
    public ModelAndView MyBlog(Integer page, Long categoryId, Integer blogStatus) {
        if (page == null || page < 1) page = 1;

        ModelAndView modelAndView = new ModelAndView("Blog/MyBlog");
        long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        List<Category> categoryLists = categoryService.categorySelectByUserId(userId);
        modelAndView.addObject("categoryLists", categoryLists);
        BlogEntity blogEntity = new BlogEntity();
        if (categoryId == null || categoryId < 1) categoryId = 0L;
        if (blogStatus == null || blogStatus < -1) blogStatus = -99;
        blogEntity.setCategoryId(categoryId);
        blogEntity.setBlogStatus(blogStatus);
        blogEntity.setUserId(userId);
        blogEntity.setContent("false");
        blogEntity.setSummary("false");
        PageHelper.startPage((page - 1) * 10, 10);
        System.out.println("page: " + page);
        modelAndView.addObject("blogLists", blogService.selectBlogByUserId(blogEntity));
        modelAndView.addObject("pageNum", blogService.selectBlogNumByUserId(blogEntity));
        return modelAndView;
    }

    @RequestMapping("BlogUpdate")
    public ModelAndView BlogUpdate(Long blogId) {
        if (blogId != null && blogId > 0) {
            long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
            BlogEntity blogEntity = blogService.selectBlogByBlogId(blogId);
            if (blogEntity == null) return new ModelAndView("redirect:/Home/404");
            List<Category> categoryLists = categoryService.categorySelectByUserId(userId);
            for (Category category : categoryLists) {
                if (category.getCategoryId() == blogEntity.getCategoryId()) {
                    ModelAndView modelAndView = new ModelAndView("Blog/UpdateBlog");
                    modelAndView.addObject("categoryLists", categoryLists);
                    modelAndView.addObject("blog", blogEntity);
                    return modelAndView;
                }
            }
        }
        return new ModelAndView("redirect:/Home/404");
    }

    @RequestMapping("HandleBlogUpdate")
    @ResponseBody
    public ResponseResult HandleBlogUpdate(BlogEntity blogEntity) {
        long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return blogService.BlogUpdate(blogEntity, userId);
    }

    @RequestMapping("HandleBlogDelete")
    @ResponseBody
    public ResponseResult HandleBlogDelete(Long blogId) {
        if (blogId == null || blogId < 0) return new ResponseResult(-1, "参数非法");
        long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return blogService.BlogDelete(blogId, userId);
    }

    @RequestMapping("HandleBlogSelectAllNormal")
    @ResponseBody
    public List<BlogEntity> HandleBlogSelectAllNormal(Integer page) {
        if (page == null || page < 1) page = 1;
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setBlogStatus(-99);
        blogEntity.setCategoryId(0);
        blogEntity.setUserId(0);
        PageHelper.startPage((page - 1) * 10, 10);
        return blogService.selectBlogAll(blogEntity);
    }


}
