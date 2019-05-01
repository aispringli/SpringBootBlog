package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.CategoryEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author SpringLee
 * @Title: CategoryController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 10:28 2019/4/19
 */
@Controller
@RequestMapping("Category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("HandleCategoryAdd")
    @ResponseBody
    public ResponseResult HandleCategoryAdd(CategoryEntity categoryEntity){
        categoryEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        return categoryService.CategoryAdd(categoryEntity);
    }

    @RequestMapping("HandleCategoryUpdateNameAndStatus")
    @ResponseBody
    public ResponseResult HandleCategoryUpdateNameAndStatus(CategoryEntity categoryEntity){
        categoryEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        return categoryService.CategoryUpdateNameAndStatus(categoryEntity);
    }

    //管理员修改状态
    @Secured({"管理员"})//此方法只允许 管理员 角色 访问
    @RequestMapping("HandleCategoryUpdateStatus")
    @ResponseBody
    public ResponseResult HandleCategoryUpdateStatus(Long categoryId){
        if(categoryId==null||categoryId<1)return new ResponseResult(1,"参数非法，处理失败");
        return categoryService.CategoryUpdateStatus(categoryId);
    }

    @RequestMapping("HandleCategoryDelete")
    @ResponseBody
    public ResponseResult HandleCategoryDelete(CategoryEntity categoryEntity){
        categoryEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        return categoryService.CategoryDelete(categoryEntity);
    }
}
