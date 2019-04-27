package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.CollectEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.mapper.blog.BlogMapper;
import com.f5tv.springbootblog.mapper.blog.CollectMapper;
import org.bouncycastle.est.LimitedSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SpringLee
 * @Title: CollectService
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:19 2019/4/25
 */
@Service
public class CollectService {

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    BlogMapper blogMapper;

    public CollectEntity selectCollectByUserIdAndBlogId(CollectEntity collectEntity) {
        return collectMapper.selectCollectByUserIdAndBlogId(collectEntity);
    }

    public ResponseResult insert(CollectEntity collectEntity) {
        if (collectMapper.selectCollectByUserIdAndBlogId(collectEntity) != null)
            return new ResponseResult(1, "您已收藏过了，请不要重复收藏");
        if (collectMapper.insert(collectEntity) > 0){
            BlogEntity blogEntity=new BlogEntity();
            blogEntity.setBlogId(collectEntity.getBlogId());
            blogEntity.setCollectQuantity(1);
            blogMapper.updateBlogQuantity(blogEntity);
            return new ResponseResult(0, true, "收藏成功");
        }
        else return new ResponseResult(-1, "收藏失败");
    }

    public ResponseResult delete(CollectEntity collectEntity) {
        if (collectMapper.selectCollectByUserIdAndBlogId(collectEntity) == null)
            return new ResponseResult(1, "您尚未收藏，删除收藏失败");
        if (collectMapper.delete(collectEntity) > 0) {
            BlogEntity blogEntity=new BlogEntity();
            blogEntity.setBlogId(collectEntity.getBlogId());
            blogEntity.setCollectQuantity(-1);
            blogMapper.updateBlogQuantity(blogEntity);
            return new ResponseResult(0, true, "成功删除收藏");
        }
        else return new ResponseResult(-1, "删除收藏失败");
    }

    public List<CollectEntity> selectCollectByUserId(long userId){
        return collectMapper.selectCollectByUserId(userId);
    }

    public int selectCollectCountByUserId(long userId){
        return collectMapper.selectCollectCountByUserId(userId);
    }
}
