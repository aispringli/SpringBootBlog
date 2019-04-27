package com.f5tv.springbootblog.entity.blog;

import java.util.Date;

/**
 * @author SpringLee
 * @Title: StarEntity
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 18:29 2019/4/25
 */
public class StarEntity {

    private long starId;

    private long userId;

    private long blogId;

    private Date starDate;

    public long getStarId() {
        return starId;
    }

    public void setStarId(long starId) {
        this.starId = starId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBlogId() {
        return blogId;
    }

    public void setBlogId(long blogId) {
        this.blogId = blogId;
    }

    public Date getStarDate() {
        return starDate;
    }

    public void setStarDate(Date starDate) {
        this.starDate = starDate;
    }
}
