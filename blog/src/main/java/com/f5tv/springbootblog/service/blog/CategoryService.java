package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.CategoryEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.mapper.blog.BlogMapper;
import com.f5tv.springbootblog.mapper.blog.CategoryMapper;
import com.f5tv.springbootblog.tools.CheckStringTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
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

    @Autowired
    BlogMapper blogMapper;

    //检查分类的名称
    ResponseResult checkCategoryName(String categoryName, long userId,long categoryId) {
        if (StringUtils.isEmpty(categoryName)) return new ResponseResult(1, "没有输入名称");
        if (categoryName.length() > 20) return new ResponseResult(2, "名称最多20个字符长度");
        if (CheckStringTool.CheckStringHasSpecialChar(categoryName)) return new ResponseResult(3, "名称含有特殊字符，请删去");
        List<CategoryEntity> categoryList = categoryMapper.categorySelectByUserId(userId);
        for (CategoryEntity category : categoryList)
            if (categoryName.equals(category.getCategoryName())&&category.getCategoryId()!=categoryId)
                return new ResponseResult(4, "已有相同名称的分类");
        return null;
    }

    public ResponseResult CategoryAdd(CategoryEntity categoryEntity) {
        ResponseResult responseResult = checkCategoryName(categoryEntity.getCategoryName(), categoryEntity.getUserId(),0);
        if (responseResult != null) return responseResult;
        categoryEntity.setCategoryStatus(0);
        categoryEntity.setBlogQuantity(0);
        if (categoryMapper.insert(categoryEntity) > 0) return new ResponseResult(0, true, "添加成功");
        return new ResponseResult(-1, "添加失败");
    }

    @Transactional
    public ResponseResult CategoryUpdateNameAndStatus(CategoryEntity categoryEntity) {
        if (categoryEntity.getCategoryStatus() != 0 && categoryEntity.getCategoryStatus() != 1)
            return new ResponseResult(-1, "非法参数，修改失败");
        ResponseResult responseResult = checkCategoryName(categoryEntity.getCategoryName(), categoryEntity.getUserId(),categoryEntity.getCategoryId());
        if (responseResult != null) return responseResult;

        CategoryEntity categoryConfirm = categoryMapper.categorySelectByCategoryId(categoryEntity.getCategoryId());
        if (categoryConfirm == null || categoryConfirm.getUserId() != categoryEntity.getUserId())
            return new ResponseResult(-3, "越权访问，修改失败");
        if ("默认".equals(categoryConfirm.getCategoryName()))
            return new ResponseResult(-2, "默认分类名称无法修改");
        try{
            if (categoryMapper.updateNameAndStatus(categoryEntity) > 0) {
                if(categoryEntity.getCategoryStatus()==categoryConfirm.getCategoryStatus()){
                    return new ResponseResult(0, true, "修改成功,只修改分类名称");
                }
                BlogEntity blogEntity = new BlogEntity();
                blogEntity.setCategoryId(categoryEntity.getCategoryId());
                blogEntity.setBlogStatus(categoryEntity.getCategoryStatus());
                blogEntity.setCategoryStatus(-1);
                long num = blogMapper.updateBlogStatus(blogEntity);
                return new ResponseResult(0, true, "修改成功,并成功修改"+num+"篇所属博客的状态");
            }
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ex.printStackTrace();
            return new ResponseResult(-4, "修改失败");
        }
        return new ResponseResult(-4, "没有进行任何修改");
    }

    @Transactional
    public ResponseResult CategoryDelete(CategoryEntity categoryEntity) {
        CategoryEntity categoryConfirm = categoryMapper.categorySelectByCategoryId(categoryEntity.getCategoryId());
        if (categoryConfirm == null || categoryConfirm.getUserId() != categoryEntity.getUserId())
            return new ResponseResult(-1, "越权访问，操作失败");
        if ("默认".equals(categoryConfirm.getCategoryName())) return new ResponseResult(-5, "删除失败,默认分类不允许删除");
        //修改该博客下面的所有分类到默认去
        try {
            if (categoryMapper.delete(categoryEntity.getCategoryId()) > 0) {
                List<CategoryEntity> categoryList = categoryMapper.categorySelectByUserId(categoryConfirm.getUserId());
                for (CategoryEntity categoryTemp : categoryList)
                    if ("默认".equals(categoryTemp.getCategoryName())) {
                        int num = blogMapper.updateBlogCategory(categoryEntity.getCategoryId(), categoryTemp.getCategoryId());
                        categoryTemp.setBlogQuantity(num);
                        categoryMapper.updateBlogQuantity(categoryTemp);
                        return new ResponseResult(0, true, "删除成功,已将" + num + "篇博客移至默认分类");
                    }
            } else return new ResponseResult(-4, "删除失败");

        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ex.printStackTrace();
            new ResponseResult(-3, "删除失败");
        }
        return new ResponseResult(-2, "删除失败");
    }

    @Transactional
    public ResponseResult CategoryUpdateStatus(CategoryEntity categoryEntity) {
        if (categoryEntity.getCategoryStatus() != 0 && categoryEntity.getCategoryStatus() != 1 && categoryEntity.getCategoryStatus() != -1)
            return new ResponseResult(-1, "非法参数，修改失败");
        if (categoryEntity.getCategoryId() < 1 || categoryEntity.getUserId() < 1) return new ResponseResult(-1, "非法参数，修改失败");
        try {
            if (categoryMapper.updateStatus(categoryEntity) > 0) {
                BlogEntity blogEntity = new BlogEntity();
                if(categoryEntity.getUserId()>0)blogEntity.setUserId(categoryEntity.getUserId());
                else blogEntity.setCategoryId(categoryEntity.getCategoryId());
                blogEntity.setBlogStatus(categoryEntity.getCategoryStatus());
                blogEntity.setCategoryStatus(0);
                long num = blogMapper.updateBlogStatus(blogEntity);
                return new ResponseResult(0, true, "修改成功,并成功修改"+num+"篇所属博客的状态");
            }
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ex.printStackTrace();
            new ResponseResult(-3, "删除失败");
        }
        return new ResponseResult(-2, "修改失败");
    }


    public List<CategoryEntity> categorySelectAll() {
        return categoryMapper.categorySelectAll();
    }

    public List<CategoryEntity> categorySelectByUserId(long userId) {
        return categoryMapper.categorySelectByUserId(userId);
    }

    CategoryEntity categorySelectByCategoryId(long categoryId) {
        return categoryMapper.categorySelectByCategoryId(categoryId);
    }

}
