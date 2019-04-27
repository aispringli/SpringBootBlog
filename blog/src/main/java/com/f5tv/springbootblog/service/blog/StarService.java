package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.StarEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.mapper.blog.BlogMapper;
import com.f5tv.springbootblog.mapper.blog.StarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author SpringLee
 * @Title: StarService
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:13 2019/4/25
 */
@Service
public class StarService {

    @Autowired
    StarMapper starMapper;

    @Autowired
    BlogMapper blogMapper;


    public StarEntity selectByUserIdAndBlogId(StarEntity starEntity){
        return starMapper.selectByUserIdAndBlogId(starEntity);
    }


    public ResponseResult insert(StarEntity starEntity) {
        if(starMapper.selectByUserIdAndBlogId(starEntity)!=null)return new ResponseResult(1,"您已点赞，请不要重复点赞");
        if(starMapper.insert(starEntity)>0){
            BlogEntity blogEntity=new BlogEntity();
            blogEntity.setBlogId(starEntity.getBlogId());
            blogEntity.setStarQuantity(1);
            blogMapper.updateBlogQuantity(blogEntity);
            return new ResponseResult(0,true,"您已成功点赞");
        }
        return new ResponseResult(-1,"点赞失败");
    }

    public ResponseResult delete(StarEntity starEntity) {
        if(starMapper.selectByUserIdAndBlogId(starEntity)==null)return new ResponseResult(1,"您尚未点赞");
        if(starMapper.delete(starEntity)>0){
            BlogEntity blogEntity=new BlogEntity();
            blogEntity.setBlogId(starEntity.getBlogId());
            blogEntity.setStarQuantity(-1);
            blogMapper.updateBlogQuantity(blogEntity);
            return new ResponseResult(0,true,"您已取消点赞");
        }
        return new ResponseResult(-1,"取消失败");
    }
}
