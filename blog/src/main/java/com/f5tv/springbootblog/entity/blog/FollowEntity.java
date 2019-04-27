package com.f5tv.springbootblog.entity.blog;

import com.f5tv.springbootblog.entity.user.UserEntity;

import java.util.Date;

/**
 * @author SpringLee
 * @Title: Follow
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 11:02 2019/4/25
 */
public class FollowEntity extends UserEntity {

    private long followId;

    private long userFollowId;

    private Date followDate;


    public long getFollowId() {
        return followId;
    }

    public void setFollowId(long followId) {
        this.followId = followId;
    }

    public long getUserFollowId() {
        return userFollowId;
    }

    public void setUserFollowId(long userFollowId) {
        this.userFollowId = userFollowId;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }
}
