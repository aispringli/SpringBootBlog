package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.CategoryEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.mapper.blog.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author SpringLee
 * @Title: BlogService
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:24 2019/4/21
 */
@Service
public class BlogService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    StarMapper starMapper;

    @Autowired
    CommentMapper commentMapper;

    ResponseResult checkBlog(BlogEntity blogEntity, long userId) {
        if (StringUtils.isEmpty(blogEntity.getTitle()) || blogEntity.getTitle().length() > 20)
            return new ResponseResult(1, "请输入长度不超过20的标题");
        if (blogEntity.getBlogStatus() != 0 && blogEntity.getBlogStatus() != 1) blogEntity.setBlogStatus(0);
        if (blogEntity.getSummary() == null) blogEntity.setSummary("");
        if (blogEntity.getBlogLogo() == null) blogEntity.setBlogLogo("");
        if (blogEntity.getContent() == null) return new ResponseResult(7, "未输入任何内容");
        if (blogEntity.getSummary().length() > 200) return new ResponseResult(2, "简介最大200字符长度");
        if (blogEntity.getBlogLogo().length() > 100) return new ResponseResult(3, "非法参数");
        if (blogEntity.getContent().length() > 16777215) return new ResponseResult(6, "内容超出最大限度");
        CategoryEntity category = categoryMapper.categorySelectByCategoryId(blogEntity.getCategoryId());
        if (category == null || category.getUserId() != userId) return new ResponseResult(4, "分类参数不合法");
        return null;
    }

    @Transactional
    public ResponseResult BlogInsert(BlogEntity blogEntity, long userId) {
        ResponseResult responseResult = checkBlog(blogEntity, userId);
        blogEntity.setBlogStatus(2);
        if (responseResult != null) return responseResult;
        try {
            if (blogMapper.blogInsert(blogEntity) > 0) {
                CategoryEntity category = new CategoryEntity();
                category.setBlogQuantity(1);
                category.setCategoryId(blogEntity.getCategoryId());
                categoryMapper.updateBlogQuantity(category);
                return new ResponseResult(0, true, "发表成功");
            } else return new ResponseResult(5, "发表失败");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return new ResponseResult(5, "发表失败,发生了异常");
        }
    }

    public List<BlogEntity> selectBlogByUserId(BlogEntity blogEntity) {
        return blogMapper.selectBlogByUserId(blogEntity);
    }

    public long selectBlogNumByUserId(BlogEntity blogEntity) {
        return blogMapper.selectBlogNumByUserId(blogEntity);
    }

    public long selectBlogAllNum(BlogEntity blogEntity) {
        return blogMapper.selectBlogAllNum(blogEntity);
    }

    public List<BlogEntity> selectBlogAll(BlogEntity blogEntity) {
        return blogMapper.selectBlogAll(blogEntity);
    }

    public BlogEntity selectBlogByBlogId(long blogId) {

        return blogMapper.selectBlogByBlogId(blogId);
    }

    @Transactional
    public ResponseResult BlogUpdate(BlogEntity blogEntity, long userId) {
        ResponseResult responseResult = checkBlog(blogEntity, userId);
        if (responseResult != null) return responseResult;
        try {
            boolean flag=false;
            BlogEntity blogEntityConfirm=blogMapper.selectBlogByBlogId(blogEntity.getBlogId());
            if (blogMapper.updateBlogContent(blogEntity) > 0)
                flag=true;
            if(blogEntityConfirm.getCategoryId()!=blogEntity.getCategoryId()){
                //同步分类的数量
                CategoryEntity category = new CategoryEntity();
                category.setBlogQuantity(1);
                category.setCategoryId(blogEntity.getCategoryId());
                categoryMapper.updateBlogQuantity(category);
                category.setBlogQuantity(-1);
                category.setCategoryId(blogEntityConfirm.getCategoryId());
                categoryMapper.updateBlogQuantity(category);
            }
            blogEntity.setCategoryId(0);
            //blogEntity.setCategoryStatus(-1); 在旧版本中控制下架的博客无法修改
            if(blogEntityConfirm.getBlogStatus()==-1)blogEntity.setBlogStatus(-1);
            else if(blogEntityConfirm.getBlogStatus()==2||blogEntityConfirm.getBlogStatus()==-2)blogEntity.setBlogStatus(2);
            //blogEntity.setBlogStatus(2);
            if(blogMapper.updateBlogStatus(blogEntity)>0)flag=true;
            if(flag) return new ResponseResult(0, true, "修改成功");
            else return new ResponseResult(5, "未进行任何修改");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return new ResponseResult(5, "修改失败,发生了异常");
        }
    }


    @Transactional
    public ResponseResult BlogDelete(long blogId, long userId) {

        try {
            //先校验是否拥有权限，删除博客后，修改分类，删除所有的评论，点赞，评论，收藏
            BlogEntity blogEntity=blogMapper.selectBlogByBlogId(blogId);
            if(blogEntity==null)return new ResponseResult(-1,"此博客不存在，无法删除");
            ResponseResult responseResult=checkBlog(blogEntity,userId);
            if(responseResult!=null)return responseResult;
            if(blogMapper.deleteBlogByBlogId(blogId)>0){
                //相应的操作
                CategoryEntity category = new CategoryEntity();
                category.setBlogQuantity(-1);
                category.setCategoryId(blogEntity.getCategoryId());
                //删除点赞，收藏，评论等
                categoryMapper.updateBlogQuantity(category);

                collectMapper.deleteByBlogId(blogId);
                starMapper.deleteByBlogId(blogId);
                commentMapper.deleteByBlogId(blogId);
                return new ResponseResult(0,true, "删除成功");
            }



        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return new ResponseResult(5, "修改失败,发生了异常");
        }
        return null;
    }

    public ResponseResult updateBlogStatus(long blogId,int blogStatus){
        BlogEntity blogEntity=blogMapper.selectBlogByBlogId(blogId);
        if(blogEntity==null)return new ResponseResult(-1,"博客不存在，处理失败");
        if(blogStatus<-2||blogStatus>2)blogStatus=0;
        blogEntity.setBlogStatus(blogStatus);

//        if(blogEntity.getBlogStatus()==2)blogEntity.setBlogStatus(0);
//        else if(blogEntity.getBlogStatus()==-1)blogEntity.setBlogStatus(0);
//        else blogEntity.setBlogStatus(-1);
//        blogEntity.setCategoryStatus(0);

        blogEntity.setCategoryId(0);
        blogEntity.setUserId(0);
        if(blogMapper.updateBlogStatus(blogEntity)>0)return new ResponseResult(0,true,"处理成功");
        else return new ResponseResult(2,"处理失败");
    }
}
