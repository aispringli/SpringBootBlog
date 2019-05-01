package com.f5tv.springbootblog.mapper.blog;

import com.f5tv.springbootblog.entity.blog.CommentEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author SpringLee
 * @Title: CommentMapper
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 16:49 2019/4/30
 */
@Mapper
@Component("CommentMapper")
public interface CommentMapper {

    @Insert("insert into comment (userId, blogId, commentContent ) values ( #{userId}, #{blogId}, #{commentContent})")
    int insert(CommentEntity commentEntity);

    @Delete("delete from comment where commentId = #{commentId}")
    int deleteByCommentId(long commentId);

    @Delete("delete from comment where blogId = #{blogId}")
    int deleteByBlogId(long blogId);

    @Update("update comment set commentStatus = #{commentStatus} where commentId = #{commentId}")
    int updateCommentStatus(CommentEntity commentEntity);


    @Select("select * from comment where commentId = #{commentId}")
    CommentEntity selectCommentByCommentId(long commentId);

    List<CommentEntity> selectAll(CommentEntity commentEntity);

    int selectAllCount(CommentEntity commentEntity);

    @Select("select * from comment where commentId = #{commentId}")
    CommentEntity selectByCommentId(long commentId);

}
