package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.CategoryEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.BlogService;
import com.f5tv.springbootblog.service.blog.CategoryService;
import com.f5tv.springbootblog.service.user.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @Autowired
    UserService userService;

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
        List<CategoryEntity> categoryLists = categoryService.categorySelectByUserId(userId);
        modelAndView.addObject("categoryLists", categoryLists);
        BlogEntity blogEntity = new BlogEntity();
        if (categoryId == null || categoryId < 1) categoryId = 0L;
        if (blogStatus == null || blogStatus < -1) blogStatus = -99;
        blogEntity.setCategoryId(categoryId);
        blogEntity.setBlogStatus(blogStatus);
        blogEntity.setUserId(userId);
        blogEntity.setContent("false");
        blogEntity.setSummary("false");
        PageHelper.startPage(page, 10);
        System.out.println("page: " + page);
        modelAndView.addObject("blogLists", blogService.selectBlogByUserId(blogEntity));
        modelAndView.addObject("pageNum", blogService.selectBlogNumByUserId(blogEntity));
        return modelAndView;
    }

    @RequestMapping("BlogDetails")
    public ModelAndView BlogDetails(Long blogId) {
        if (blogId == null) return new ModelAndView("/Error/404");
        BlogEntity blogEntity = blogService.selectBlogByBlogId(blogId);
        if (blogEntity == null) return new ModelAndView("/Error/404");
        //非公开的只能个人或管理员浏览
        if(blogEntity.getBlogStatus() != 0){
            if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserEntity){
                UserEntity userEntity = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                if(!userEntity.getAuthorities().contains(new SimpleGrantedAuthority("管理员"))&&blogEntity.getBlogId()!=userEntity.getUserId())return new ModelAndView("/Error/404");
            }
            else return new ModelAndView("/Error/404");
        }

        List<CategoryEntity> categoryLists = categoryService.categorySelectByUserId(blogEntity.getUserId());
        ModelAndView modelAndView = new ModelAndView("Blog/BlogDetails");
        modelAndView.addObject("categoryLists", categoryLists);
        modelAndView.addObject("blogEntity", blogEntity);
        return modelAndView;
    }

    @RequestMapping("BlogDashboard")
    public ModelAndView BlogDashboard(Long userId, Long categoryId, Integer page) {
        if (page == null || page < 1) page = 1;
        if (userId == null) return new ModelAndView("/Error/404");
        UserEntity userEntity = userService.userEntitySelectByUserId(userId);
        if (userEntity == null) return new ModelAndView("/Error/404");
        ModelAndView modelAndView = new ModelAndView("Blog/BlogDashboard");
        modelAndView.addObject("userEntity", userEntity);
        List<CategoryEntity> categoryLists = categoryService.categorySelectByUserId(userId);

        modelAndView.addObject("categoryLists", categoryLists);
        PageHelper.startPage(1, 10);
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setUserId(userId);
        if (categoryId != null) {
            blogEntity.setCategoryId(categoryId);
            modelAndView.addObject("categoryId", categoryId);
        } else {
            blogEntity.setCategoryId(0);
            modelAndView.addObject("categoryId", 0);
        }
        blogEntity.setBlogStatus(0);
        blogEntity.setContent("false");
        PageHelper.startPage(page, 10);
        List<BlogEntity> blogLists = blogService.selectBlogByUserId(blogEntity);
        modelAndView.addObject("blogLists", blogLists);
        return modelAndView;
    }

    @RequestMapping("BlogUpdate")
    public ModelAndView BlogUpdate(Long blogId) {
        if (blogId != null && blogId > 0) {
            long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
            BlogEntity blogEntity = blogService.selectBlogByBlogId(blogId);
            if (blogEntity == null) return new ModelAndView("redirect:/Error/404");
            List<CategoryEntity> categoryLists = categoryService.categorySelectByUserId(userId);
            for (CategoryEntity category : categoryLists) {
                if (category.getCategoryId() == blogEntity.getCategoryId()) {
                    ModelAndView modelAndView = new ModelAndView("Blog/UpdateBlog");
                    modelAndView.addObject("categoryLists", categoryLists);
                    modelAndView.addObject("blog", blogEntity);
                    return modelAndView;
                }
            }
        }
        return new ModelAndView("redirect:/Error/404");
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
        if (blogId == null || blogId < 0) return new ResponseResult(-1, "参数非法,处理失败");
        long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return blogService.BlogDelete(blogId, userId);
    }


    @RequestMapping("HandleBlogSelectAllNormalAjax")
    public ModelAndView HandleBlogSelectAllNormalAjax(Integer page, Long userId, Long categoryId) {
        ModelAndView modelAndView = new ModelAndView("/Blog/blogListsTemplate");
        if (page == null || page < 1) page = 1;
        BlogEntity blogEntity = new BlogEntity();
        if (userId != null && userId > 0) {
            blogEntity.setUserId(userId);
            if (categoryId != null && categoryId > 0) blogEntity.setCategoryId(categoryId);
            else blogEntity.setCategoryId(0);
        }
        PageHelper.startPage(page, 10);
        modelAndView.addObject("blogLists", blogService.selectBlogAll(blogEntity));
        return modelAndView;
    }

    @RequestMapping("HandleUpdateBlogStatus")
    @ResponseBody
    public ResponseResult HandleUpdateBlogStatus(Long blogId){
        if(blogId==null||blogId<1)return new ResponseResult(1,"参数非法，处理失败");
        return blogService.updateBlogStatus(blogId);
    }

}
