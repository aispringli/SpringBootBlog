package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import com.f5tv.springbootblog.entity.blog.CommentEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.mapper.blog.BlogMapper;
import com.f5tv.springbootblog.mapper.blog.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SpringLee
 * @Title: CommentService
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 16:49 2019/4/30
 */
@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    BlogMapper blogMapper;

    public ResponseResult commentAdd(CommentEntity commentEntity) {
        if (commentEntity.getCommentContent().length() > 500) return new ResponseResult(1, "评论内容最大长度500个字符");
        if(blogMapper.selectBlogByBlogId(commentEntity.getBlogId())==null)return new ResponseResult(2, "博客不存在，评论失败");
        if (commentMapper.insert(commentEntity) > 0){
            BlogEntity blogEntity=new BlogEntity();
            blogEntity.setBlogId(commentEntity.getBlogId());
            blogEntity.setCollectQuantity(0);
            blogEntity.setStarQuantity(0);
            blogEntity.setCommentQuantity(1);
            blogMapper.updateBlogQuantity(blogEntity);
            return new ResponseResult(0, true, "评论成功");
        }
        else return new ResponseResult(-1, "评论成功");
    }

    public ResponseResult commentDeleteByCommentId(long commentId, long userId) {
        CommentEntity commentEntity=commentMapper.selectCommentByCommentId(commentId);
        if(commentEntity==null)return new ResponseResult(-1,"评论为空，处理失败");
        if(commentEntity.getUserId()!=userId)return new ResponseResult(-2,"非法越权访问");
        if(commentMapper.deleteByCommentId(commentId)>0){
            BlogEntity blogEntity=new BlogEntity();
            blogEntity.setBlogId(commentEntity.getBlogId());
            blogEntity.setCollectQuantity(0);
            blogEntity.setStarQuantity(0);
            blogEntity.setCommentQuantity(-1);
            blogMapper.updateBlogQuantity(blogEntity);
            return new ResponseResult(0,true,"删除评论成功");
        }
        else return new ResponseResult(-3, "删除评论失败");
    }


    public List<CommentEntity> selectComment(CommentEntity commentEntity){
        return commentMapper.selectAll(commentEntity);
    }

    public int selectCommentCount(CommentEntity commentEntity){
        return commentMapper.selectAllCount(commentEntity);
    }

    public int deleteByBlogId(long blogId){
        return commentMapper.deleteByBlogId(blogId);
    }

    public ResponseResult UpdateCommentStatus(long commentId){
        CommentEntity commentEntity=commentMapper.selectByCommentId(commentId);
        if(commentEntity.getCommentStatus()==0)commentEntity.setCommentStatus(-1);
        else commentEntity.setCommentStatus(0);
        if(commentEntity==null)return new ResponseResult(-1,"评论不存在，处理失败");
        if(commentMapper.updateCommentStatus(commentEntity)>0)return new ResponseResult(0,true,"处理成功");
        else return new ResponseResult(-2,"处理失败");
    }

}
