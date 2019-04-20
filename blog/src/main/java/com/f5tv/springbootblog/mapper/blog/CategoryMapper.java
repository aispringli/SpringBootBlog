package com.f5tv.springbootblog.mapper.blog;

import com.f5tv.springbootblog.entity.blog.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author SpringLee
 * @Title: CategoryMapper
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 10:28 2019/4/19
 */
@Mapper
@Component("CategoryMapper")
public interface CategoryMapper {

    @Results(id = "categoryResultMap", value =
            {
                    @Result(property = "userId", column = "userId"),
                    @Result(property = "categoryId", column = "categoryId"),
                    @Result(property = "categoryName", column = "categoryName"),
                    @Result(property = "categoryStatus", column = "categoryStatus"),
                    @Result(property = "categoryDate", column = "categoryDate"),
                    @Result(property = "blogQuantity", column = "blogQuantity")
            })

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Insert("INSERT INTO category(userId,categoryName, categoryStatus,blogQuantity) " +
            "VALUES (#{userId},#{categoryName}, #{categoryStatus}, #{blogQuantity})")
    @Options(useGeneratedKeys = true, keyProperty = "categoryId", keyColumn = "categoryId")
    int insert(Category category);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Update("update category set categoryName=#{categoryName},categoryStatus=#{categoryStatus} where categoryId = #{categoryId}")
    int updateNameAndStatus(Category category);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Update("update category set categoryStatus=#{categoryStatus} where categoryId = #{categoryId}")
    int updateStatus(Category category);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Update("delete from category  where categoryId = #{categoryId}")
    int delete(long categoryId);


    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from category order by categoryId")
    List<Category> categorySelectAll();

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from category where userId = #{userId} order by categoryId")
    List<Category> categorySelectByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from category where categoryId = #{categoryId}")
    Category categorySelectByCategoryId(long categoryId);
}
