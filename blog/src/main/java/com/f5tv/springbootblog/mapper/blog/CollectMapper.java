package com.f5tv.springbootblog.mapper.blog;

import com.f5tv.springbootblog.entity.blog.CollectEntity;
import com.f5tv.springbootblog.entity.core.DateQuantityEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author SpringLee
 * @Title: CollectMapper
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:11 2019/4/25
 */
@Mapper
@Component("CollectMapper")
public interface CollectMapper {

    @Insert("insert into collect (userId,blogId) values (#{userId},#{blogId})")
    int insert (CollectEntity collectEntity);

    @Select("select * from collect where userId = #{userId} and blogId = #{blogId}")
    CollectEntity selectCollectByUserIdAndBlogId(CollectEntity collectEntity);

    @Select("select collectId,collectDate,blog.blogId,title,starQuantity,collectQuantity,commentQuantity,category.categoryId,categoryName,collect.userId,username from collect left join blog on collect.blogId = blog.blogId " +
            "left join category on blog.categoryId = category.categoryId left join user on category.userId=user.userId where collect.userId = #{userId} and blogStatus = 0 order by collectId desc")
    List<CollectEntity> selectCollectByUserId(long userId);

    @Select("select count(*) from collect where userId = #{userId}")
    int selectCollectCountByUserId(long userId);

    @Delete("delete from collect where userId = #{userId} and blogId = #{blogId}")
    int delete(CollectEntity collectEntity);

    @Delete("delete from collect where blogId = #{blogId}")
    int deleteByBlogId(long blogId);

    @Select("select count(0) from collect")
    long collectCount();

    @Select("SELECT DATE_FORMAT(collectDate,'%Y-%m-%d') as time,count(0) as quantity FROM collect where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(collectDate)  GROUP BY  time")
    List<DateQuantityEntity> collectCountWeek();
}
