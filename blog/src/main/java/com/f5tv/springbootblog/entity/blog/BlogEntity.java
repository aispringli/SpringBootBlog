package com.f5tv.springbootblog.entity.blog;

import java.util.Date;

/**
 * @author SpringLee
 * @Title: BlogEntity
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:15 2019/4/21
 */
public class BlogEntity extends CategoryEntity {

    //主键id
    private long blogId;

    //标题
    private String title;

    // 简介
    private String summary;

    //logo 图片地址
    private String blogLogo;

    //内容
    private String content;

    //发表日期
    private Date blogDate;

    //状态 默认 0
    private int blogStatus;

    //点星量
    private long starQuantity;

    //收藏量
    private long collectQuantity;

    //评论量
    private long commentQuantity;

    public long getBlogId() {
        return blogId;
    }

    public void setBlogId(long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBlogLogo() {
        return blogLogo;
    }

    public void setBlogLogo(String blogLogo) {
        this.blogLogo = blogLogo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(Date blogDate) {
        this.blogDate = blogDate;
    }

    public int getBlogStatus() {
        return blogStatus;
    }

    public void setBlogStatus(int blogStatus) {
        this.blogStatus = blogStatus;
    }

    public long getStarQuantity() {
        return starQuantity;
    }

    public void setStarQuantity(long starQuantity) {
        this.starQuantity = starQuantity;
    }

    public long getCollectQuantity() {
        return collectQuantity;
    }

    public void setCollectQuantity(long collectQuantity) {
        this.collectQuantity = collectQuantity;
    }

    public long getCommentQuantity() {
        return commentQuantity;
    }

    public void setCommentQuantity(long commentQuantity) {
        this.commentQuantity = commentQuantity;
    }
}
