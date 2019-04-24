package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.Category;
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
        List<Category> categoryList = categoryMapper.categorySelectByUserId(userId);
        for (Category category : categoryList)
            if (categoryName.equals(category.getCategoryName())&&category.getCategoryId()!=categoryId)
                return new ResponseResult(4, "已有相同名称的分类");
        return null;
    }

    public ResponseResult CategoryAdd(Category category) {
        ResponseResult responseResult = checkCategoryName(category.getCategoryName(), category.getUserId(),0);
        if (responseResult != null) return responseResult;
        category.setCategoryStatus(0);
        category.setBlogQuantity(0);
        if (categoryMapper.insert(category) > 0) return new ResponseResult(0, true, "添加成功");
        return new ResponseResult(-1, "添加失败");
    }

    @Transactional
    public ResponseResult CategoryUpdateNameAndStatus(Category category) {
        if (category.getCategoryStatus() != 0 && category.getCategoryStatus() != 1)
            return new ResponseResult(-1, "非法参数，修改失败");
        ResponseResult responseResult = checkCategoryName(category.getCategoryName(), category.getUserId(),category.getCategoryId());
        if (responseResult != null) return responseResult;

        Category categoryConfirm = categoryMapper.categorySelectByCategoryId(category.getCategoryId());
        if (categoryConfirm == null || categoryConfirm.getUserId() != category.getUserId())
            return new ResponseResult(-3, "越权访问，修改失败");
        if ("默认".equals(categoryConfirm.getCategoryName()) && categoryConfirm.getCategoryName() != category.getCategoryName())
            return new ResponseResult(-2, "默认分类名称无法修改");
        try{
            if (categoryMapper.updateNameAndStatus(category) > 0) {
                BlogEntity blogEntity = new BlogEntity();
                blogEntity.setCategoryId(category.getCategoryId());
                blogEntity.setBlogStatus(category.getCategoryStatus());
                blogEntity.setCategoryStatus(-1);
                long num = blogMapper.updateBlogStatus(blogEntity);
                return new ResponseResult(0, true, "修改成功,并成功修改"+num+"篇所属博客的状态");
            }
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ex.printStackTrace();
            return new ResponseResult(-4, "修改失败");
        }
        return new ResponseResult(-4, "修改失败");
    }

    @Transactional
    public ResponseResult CategoryDelete(Category category) {
        Category categoryConfirm = categoryMapper.categorySelectByCategoryId(category.getCategoryId());
        if (categoryConfirm == null || categoryConfirm.getUserId() != category.getUserId())
            return new ResponseResult(-1, "越权访问，操作失败");
        if ("默认".equals(categoryConfirm.getCategoryName())) return new ResponseResult(-5, "删除失败,默认分类不允许删除");
        //修改该博客下面的所有分类到默认去
        try {
            if (categoryMapper.delete(category.getCategoryId()) > 0) {
                List<Category> categoryList = categoryMapper.categorySelectByUserId(categoryConfirm.getUserId());
                for (Category categoryTemp : categoryList)
                    if ("默认".equals(categoryTemp.getCategoryName())) {
                        int num = blogMapper.updateBlogCategory(category.getCategoryId(), categoryTemp.getCategoryId());
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
    public ResponseResult CategoryUpdateStatus(Category category) {
        if (category.getCategoryStatus() != 0 && category.getCategoryStatus() != 1 && category.getCategoryStatus() != -1)
            return new ResponseResult(-1, "非法参数，修改失败");
        if (category.getCategoryId() < 1 || category.getUserId() < 1) return new ResponseResult(-1, "非法参数，修改失败");
        try {
            if (categoryMapper.updateStatus(category) > 0) {
                BlogEntity blogEntity = new BlogEntity();
                if(category.getUserId()>0)blogEntity.setUserId(category.getUserId());
                else blogEntity.setCategoryId(category.getCategoryId());
                blogEntity.setBlogStatus(category.getCategoryStatus());
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


    public List<Category> categorySelectAll() {
        return categoryMapper.categorySelectAll();
    }

    public List<Category> categorySelectByUserId(long userId) {
        return categoryMapper.categorySelectByUserId(userId);
    }

    Category categorySelectByCategoryId(long categoryId) {
        return categoryMapper.categorySelectByCategoryId(categoryId);
    }

}
