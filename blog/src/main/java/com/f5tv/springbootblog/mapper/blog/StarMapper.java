package com.f5tv.springbootblog.mapper.blog;

import com.f5tv.springbootblog.entity.blog.StarEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

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

    @Delete("delete from star where and blogId = #{blogId}")
    int deleteByBlogId(long blogId);

}
