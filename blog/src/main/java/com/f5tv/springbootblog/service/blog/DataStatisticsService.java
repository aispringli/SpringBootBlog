package com.f5tv.springbootblog.service.blog;

import com.f5tv.springbootblog.entity.core.DateQuantityEntity;
import com.f5tv.springbootblog.mapper.blog.*;
import com.f5tv.springbootblog.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SpringLee
 * @Title: DataStatisticsService
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 15:05 2019/5/20
 */
@Service
public class DataStatisticsService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    StarMapper starMapper;


    public long userCount(){
        return userMapper.userCount();
    }

    public List<DateQuantityEntity> userCountWeek(){
        return userMapper.userCountWeek();
    }


    public long categoryCount(){
        return categoryMapper.categoryCount();
    }

    public List<DateQuantityEntity> categoryCountWeek(){
        return categoryMapper.categoryCountWeek();
    }

    public long blogCount(){
        return blogMapper.blogCount();
    }

    public List<DateQuantityEntity> blogCountWeek(){
        return blogMapper.blogCountWeek();
    }

    public long commentCount(){
        return commentMapper.commentCount();
    }

    public List<DateQuantityEntity> commentCountWeek(){
        return commentMapper.commentCountWeek();
    }

    public long followCount(){
        return followMapper.followCount();
    }

    public List<DateQuantityEntity> followCountWeek(){
        return followMapper.followCountWeek();
    }

    public long collectCount(){
        return collectMapper.collectCount();
    }

    public List<DateQuantityEntity> collectCountWeek(){
        return collectMapper.collectCountWeek();
    }

    public long starCount(){
        return starMapper.starCount();
    }

    public List<DateQuantityEntity> starCountWeek(){
        return starMapper.starCountWeek();
    }

}
