package com.f5tv.springbootblog.mapper.blog;

import com.f5tv.springbootblog.entity.blog.BlogEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author SpringLee
 * @Title: BlogMapper
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:24 2019/4/21
 */
@Mapper
@Component("BlogMapper")
public interface BlogMapper {

    @Results(id = "blogResultMap", value =
            {
                    @Result(property = "userId", column = "userId"),
                    @Result(property = "username", column = "username"),
                    @Result(property = "categoryId", column = "categoryId"),
                    @Result(property = "categoryName", column = "categoryName"),
                    @Result(property = "categoryStatus", column = "categoryStatus"),
                    @Result(property = "categoryDate", column = "categoryDate"),
                    @Result(property = "blogQuantity", column = "blogQuantity"),

                    @Result(property = "blogId", column = "blogId"),
                    @Result(property = "title", column = "title"),
                    @Result(property = "summary", column = "summary"),
                    @Result(property = "blogLogo", column = "blogLogo"),
                    @Result(property = "content", column = "content"),
                    @Result(property = "blogDate", column = "blogDate"),
                    @Result(property = "blogStatus", column = "blogStatus"),
                    @Result(property = "starQuantity", column = "starQuantity"),
                    @Result(property = "collectQuantity", column = "collectQuantity"),
                    @Result(property = "commentQuantity", column = "commentQuantity")
            })

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Insert("INSERT INTO blog(title, summary, blogLogo, content, categoryId) " +
            "VALUES (#{title}, #{summary}, #{blogLogo}, #{content}, #{categoryId})")
    @Options(useGeneratedKeys = true, keyProperty = "blogId", keyColumn = "blogId")
    int blogInsert(BlogEntity blogEntity);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from blog where blogId = #{blogId}")
    BlogEntity selectBlogByBlogId(long blogId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from blog where categoryId = #{categoryId} order by blogId desc")
    List<BlogEntity> selectBlogByCategoryId(long categoryId);


    @Lock(LockModeType.PESSIMISTIC_READ)
    List<BlogEntity> selectBlogByUserId(BlogEntity blogEntity);

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<BlogEntity> selectBlogAll(BlogEntity blogEntity);

    @Lock(LockModeType.PESSIMISTIC_READ)
    long selectBlogNumByUserId(BlogEntity blogEntity);

    @Lock(LockModeType.PESSIMISTIC_READ)
    long selectBlogAllNum(BlogEntity blogEntity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Update("update blog set categoryId = #{newCategoryId} where categoryId = #{oldCategoryId}")
    int updateBlogCategory(@Param("oldCategoryId") long oldCategoryId, @Param("newCategoryId") long newCategoryId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Update("update blog set blogStatus = #{blogStatus} where blogId = #{blogId}")
    int updateBlogStatus(BlogEntity blogEntity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Update("update blog set title = #{title}, summary = #{summary}, blogLogo = #{blogLogo}, content = #{content}, " +
            "categoryId = #{categoryId} where blogId = #{blogId}")
    int updateBlogContent(BlogEntity blogEntity);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Update("update blog set starQuantity = starQuantity + #{starQuantity}, collectQuantity = collectQuantity + #{collectQuantity}, " +
            "commentQuantity = commentQuantity + #{commentQuantity} where blogId = #{blogId}")
    int updateBlogQuantity(BlogEntity blogEntity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Delete("delete from blog  where blogId = #{blogId}")
    int deleteBlogByBlogId(long blogId);


}
