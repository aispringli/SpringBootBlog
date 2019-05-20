package com.f5tv.springbootblog.mapper.blog;

import com.f5tv.springbootblog.entity.blog.FollowEntity;
import com.f5tv.springbootblog.entity.core.DateQuantityEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author SpringLee
 * @Title: FollowMapper
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 11:06 2019/4/25
 */
@Mapper
@Component("FollowMapper")
public interface FollowMapper {

    @Insert("insert into follow (userId,userFollowId) values(#{userId},#{userFollowId})")
    int insert(FollowEntity followEntity);

    @Delete("delete from follow where userId = #{userId} and userFollowId =#{userFollowId}")
    int deleteByFollowId(FollowEntity followEntity);

    @Select("select * from follow where userId =#{userId} and userFollowId =#{userFollowId}")
    FollowEntity selectUserIsFollow(FollowEntity followEntity);

    //查询某个用户关注了哪些人
    @Select("select followId,follow.userId,userFollowId,followDate,username,userLogoSrc,userMotto from follow left join user on follow.userId = user.userId where follow.userFollowId =#{userFollowId} and userStatus = 0 order by followId desc")
    List<FollowEntity> selectUserFollowByUserFollowId(long userFollowId);

    @Select("select count(*) from follow where userFollowId =#{userFollowId}")
    int selectCountUserFollowByUserFollowId(long userFollowId);


    //查询某个用户被哪些人关注
    @Select("select followId,follow.userId,userFollowId,followDate,username,userLogoSrc,userMotto from follow left join user on follow.userFollowId = user.userId where follow.userId =#{userId} and userStatus = 0 order by followId desc")
    List<FollowEntity> selectFollowersByUserId(long userId);

    //查询某个用户被哪些人关注
    @Select("select count(*) from follow where userId =#{userId}")
    int selectCountFollowersByUserId(long userId);

    @Select("select count(0) from follow")
    long followCount();

    @Select("SELECT DATE_FORMAT(followDate,'%Y-%m-%d') as time,count(0) as quantity FROM follow where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(followDate)  GROUP BY  time")
    List<DateQuantityEntity> followCountWeek();
}
