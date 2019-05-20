package com.f5tv.springbootblog.mapper.blog;

import com.f5tv.springbootblog.entity.blog.StarEntity;
import com.f5tv.springbootblog.entity.core.DateQuantityEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author SpringLee
 * @Title: StarMapper
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:06 2019/4/25
 */
@Mapper
@Component("StarMapper")
public interface StarMapper {

    @Insert("insert into star (userId,blogId) values (#{userId},#{blogId})")
    int insert(StarEntity starEntity);

    @Select("select * from star where userId = #{userId} and blogId = #{blogId}")
    StarEntity selectByUserIdAndBlogId(StarEntity starEntity);

    @Delete("delete from star where userId = #{userId} and blogId = #{blogId}")
    int delete(StarEntity starEntity);

    @Delete("delete from star where blogId = #{blogId}")
    int deleteByBlogId(long blogId);

    @Select("select count(0) from star")
    long starCount();

    @Select("SELECT DATE_FORMAT(starDate,'%Y-%m-%d') as time,count(0) as quantity FROM star where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(starDate)  GROUP BY  time")
    List<DateQuantityEntity> starCountWeek();
}
