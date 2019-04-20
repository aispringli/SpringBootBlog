package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.blog.Category;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.mapper.blog.CategoryMapper;
import com.f5tv.springbootblog.tools.CheckStringTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author SpringLee
 * @Title: CategoryService
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 10:27 2019/4/19
 */
@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    //检查分类的名称
    ResponseResult checkCategoryName(String categoryName,long userId){
        if(StringUtils.isEmpty(categoryName))return new ResponseResult(1,"没有输入名称");
        if(categoryName.length()>20)return new ResponseResult(2,"名称最多20个字符长度");
        if(CheckStringTool.CheckStringHasSpecialChar(categoryName))return new ResponseResult(3,"名称含有特殊字符，请删去");
        List<Category> categoryList=categoryMapper.categorySelectByUserId(userId);
        for (Category category : categoryList)
            if (categoryName.equals(category.getCategoryName()))
                return new ResponseResult(4, "已有相同名称的分类");
        return null;
    }

    public ResponseResult CategoryAdd(Category category){
        ResponseResult responseResult=checkCategoryName(category.getCategoryName(),category.getUserId());
        if(responseResult!=null)return responseResult;
        category.setCategoryStatus(0);
        category.setBlogQuantity(0);
        if(categoryMapper.insert(category)>0)return new ResponseResult(0,true,"添加成功");
        return new ResponseResult(-1,"添加失败");
    }

    public ResponseResult CategoryUpdateNameAndStatus(Category category){
        if(category.getCategoryStatus()!=0&&category.getCategoryStatus()!=1)return new ResponseResult(-1,"非法参数，修改失败");
        ResponseResult responseResult=checkCategoryName(category.getCategoryName(),category.getUserId());
        if(responseResult!=null)return responseResult;
        if("默认".equals(category.getCategoryName()))return new ResponseResult(-2,"默认分类无法删除");
        Category categoryConfirm=categoryMapper.categorySelectByCategoryId(category.getCategoryId());
        if(categoryConfirm==null||categoryConfirm.getUserId()!=category.getUserId())return new ResponseResult(-3,"越权访问，修改失败");
        if(categoryMapper.updateNameAndStatus(category)>0)return new ResponseResult(0,true,"修改成功");
        return new ResponseResult(-4,"修改失败");
    }

    public ResponseResult CategoryDelete(Category category){
        Category categoryConfirm=categoryMapper.categorySelectByCategoryId(category.getCategoryId());
        if(categoryConfirm==null||categoryConfirm.getUserId()!=category.getUserId())return new ResponseResult(-1,"越权访问，操作失败");
        //修改该博客下面的所有分类到默认去


        if(categoryMapper.delete(category.getCategoryId())>0)return new ResponseResult(0,true,"删除成功");
        return new ResponseResult(-2,"删除失败");
    }

    public ResponseResult CategoryUpdateStatus(Category category){
        if(category.getCategoryStatus()!=0&&category.getCategoryStatus()!=1&&category.getCategoryStatus()!=-1)return new ResponseResult(-1,"非法参数，修改失败");
        if(categoryMapper.updateStatus(category)>0)return new ResponseResult(0,true,"修改成功");
        return new ResponseResult(-2,"修改失败");
    }


    public List<Category> categorySelectAll(){
        return categoryMapper.categorySelectAll();
    }

    public List<Category> categorySelectByUserId(long userId){
        return categoryMapper.categorySelectByUserId(userId);
    }

    Category categorySelectByCategoryId(long categoryId){
        return categoryMapper.categorySelectByCategoryId(categoryId);
    }

}
