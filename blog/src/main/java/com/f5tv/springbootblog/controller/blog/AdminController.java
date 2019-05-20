package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.CategoryEntity;
import com.f5tv.springbootblog.entity.blog.CommentEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.BlogService;
import com.f5tv.springbootblog.service.blog.CategoryService;
import com.f5tv.springbootblog.service.blog.CommentService;
import com.f5tv.springbootblog.service.blog.DataStatisticsService;
import com.f5tv.springbootblog.service.user.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    @Autowired
    DataStatisticsService dataStatisticsService;

    @RequestMapping("Index")
    @PreAuthorize("hasAnyAuthority('管理员')")//此方法只允许 管理员 角色 访问
    public ModelAndView Index() {
        ModelAndView modelAndView = new ModelAndView("Admin/Index");
        //统计方面

        modelAndView.addObject("userCount",dataStatisticsService.userCount());
        modelAndView.addObject("categoryCount",dataStatisticsService.categoryCount());
        modelAndView.addObject("blogCount",dataStatisticsService.blogCount());
        modelAndView.addObject("commentCount",dataStatisticsService.commentCount());
        modelAndView.addObject("followCount",dataStatisticsService.followCount());
        modelAndView.addObject("commentCount",dataStatisticsService.commentCount());
        modelAndView.addObject("starCount",dataStatisticsService.starCount());

        modelAndView.addObject("userCountWeek",dataStatisticsService.userCountWeek());
        modelAndView.addObject("categoryCountWeek",dataStatisticsService.categoryCountWeek());
        modelAndView.addObject("blogCountWeek",dataStatisticsService.blogCountWeek());
        modelAndView.addObject("commentCountWeek",dataStatisticsService.commentCountWeek());
        modelAndView.addObject("followCountWeek",dataStatisticsService.followCountWeek());
        modelAndView.addObject("commentCountWeek",dataStatisticsService.commentCountWeek());
        modelAndView.addObject("starCountWeek",dataStatisticsService.starCountWeek());

        return modelAndView;
    }

    @RequestMapping("UserManger")
    @PreAuthorize("hasAnyAuthority('管理员')")//此方法只允许 管理员 角色 访问
    public ModelAndView UserManger(Integer page, Integer userRoleId, Integer userStatus, String username, String userEmail) {
        if (page == null || page < 1) page = 1;
        if (userStatus == null) userStatus = -99;
        UserEntity userEntity = new UserEntity();
        int userRoleIdTemp = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserRoleId();
        if (userRoleId == null || userRoleId == 0) {
            if (userRoleIdTemp == 101) userEntity.setUserRoleId(0);
            else userEntity.setUserRoleId(301);
        } else {
            if (userRoleIdTemp < userRoleId) userEntity.setUserRoleId(userRoleId);
            else userEntity.setUserRoleId(301);
        }
        userEntity.setUserStatus(userStatus);
        if (!StringUtils.isEmpty(username)) username = "%" + username + "%";
        userEntity.setUsername(username);
        if (!StringUtils.isEmpty(userEmail)) userEmail = "%" + userEmail + "%";
        userEntity.setUserEmail(userEmail);
        ModelAndView modelAndView = new ModelAndView("Admin/UserManger");

        PageHelper.startPage(page, 10);
        modelAndView.addObject("userLists", userService.selectAllUser(userEntity));
        modelAndView.addObject("pageNum", userService.selectAllUserCount(userEntity));
        return modelAndView;
    }


    @RequestMapping("CategoryManger")
    @PreAuthorize("hasAnyAuthority('管理员')")//此方法只允许 管理员 角色 访问
    public ModelAndView CategoryManger(Integer page, Long userId, Integer categoryStatus) {
        ModelAndView modelAndView = new ModelAndView("Admin/CategoryManger");
        CategoryEntity categoryEntity = new CategoryEntity();
        if (page == null || page < 1) page = 1;
        if (userId == null || userId < 1) userId = 0L;
        if (categoryStatus == null) categoryStatus = -99;
        categoryEntity.setUserId(userId);
        categoryEntity.setCategoryStatus(categoryStatus);
        PageHelper.startPage(page, 10);
        modelAndView.addObject("categoryLists", categoryService.categorySelectAll(categoryEntity));
        modelAndView.addObject("pageNum", categoryService.categorySelectAllCount(categoryEntity));
        return modelAndView;
    }


    @RequestMapping("BlogManger")
    @PreAuthorize("hasAnyAuthority('管理员')")//此方法只允许 管理员 角色 访问
    public ModelAndView BlogManger(Integer blogStatus, Integer page, Long userId, Long categoryId, String title) {
        if (blogStatus == null) blogStatus = -99;
        if (page == null || page < 1) page = 1;
        if (userId == null || userId < 1) userId = 0L;
        if (categoryId == null || categoryId < 1) categoryId = 0L;
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setCategoryId(categoryId);
        blogEntity.setUserId(userId);
        blogEntity.setBlogStatus(blogStatus);
        if (!StringUtils.isEmpty(title)) title = "%" + title + "%";
        blogEntity.setTitle(title);
        ModelAndView modelAndView = new ModelAndView("Admin/BlogManger");
        PageHelper.startPage(page,10);
        modelAndView.addObject("blogLists",blogService.selectBlogAll(blogEntity));
        modelAndView.addObject("pageNum", blogService.selectBlogAllNum(blogEntity));
        return modelAndView;
    }

    @RequestMapping("CommentManger")
    @PreAuthorize("hasAnyAuthority('管理员')")//此方法只允许 管理员 角色 访问
    public ModelAndView CommentManger(Integer page,Long userId,Long blogId,Integer commentStatus ,String commentContent) {
        if (page == null || page < 1) page = 1;
        CommentEntity commentEntity=new CommentEntity();
        if(userId==null||userId<1)commentEntity.setUserId(0);
        else commentEntity.setUserId(userId);
        if(blogId==null||blogId<1)commentEntity.setBlogId(0);
        else commentEntity.setBlogId(blogId);
        if(!StringUtils.isEmpty(commentContent))commentEntity.setCommentContent("%"+commentContent+"%");
        if(commentStatus==null)commentEntity.setCommentStatus(-99);
        else commentEntity.setCommentStatus(commentStatus);
        PageHelper.startPage(page,10);
        ModelAndView modelAndView = new ModelAndView("Admin/CommentManger");
        modelAndView.addObject("commentLists",commentService.selectComment(commentEntity));
        modelAndView.addObject("pageNum",commentService.selectCommentCount(commentEntity));
        return modelAndView;
    }
}
