package com.f5tv.springbootblog.entity.blog;

import java.util.Date;

/**
 * @author SpringLee
 * @Title: CollectEntity
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 18:28 2019/4/25
 */
public class CollectEntity extends BlogEntity {

    private long collectId;

    private long userId;

    private Date collectDate;

    public long getCollectId() {
        return collectId;
    }

    public void setCollectId(long collectId) {
        this.collectId = collectId;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
    }
}
