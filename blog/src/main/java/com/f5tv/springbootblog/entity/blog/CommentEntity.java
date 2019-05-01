package com.f5tv.springbootblog.entity.blog;

import com.f5tv.springbootblog.entity.user.UserEntity;

import java.util.Date;

/**
 * @author SpringLee
 * @Title: CommentEntity
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 16:43 2019/4/30
 */
public class CommentEntity extends UserEntity {

    private long commentId;

    private long blogId;

    private String title;

    private String commentContent;

    private Date commentDate;

    private int commentStatus;

    private int floorNum;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getBlogId() {
        return blogId;
    }

    public void setBlogId(long blogId) {
        this.blogId = blogId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
