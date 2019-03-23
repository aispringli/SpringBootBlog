package com.f5tv.springbootblog.mapper.user;

import com.f5tv.springbootblog.entity.user.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author 34499
 * @Title: UserMapper
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 11:01 2019/3/12
 */
@Mapper()
@Component(value = "UserMapper")
public interface UserMapper {
    @Results(id = "userRoleResultMap", value =
            {
                    @Result(property = "userId", column = "userId"),
                    @Result(property = "userName", column = "userName"),
                    @Result(property = "userRoleName", column = "userRoleName"),
                    @Result(property = "userEmail", column = "userEmail"),
                    @Result(property = "userStatus", column = "userStatus"),
                    @Result(property = "userDate", column = "userDate"),
                    @Result(property = "userLogoSrc", column = "userLogoSrc"),
                    @Result(property = "userMotto", column = "userMotto")
            })

    @Select("select * from user where userName = #{userName}")
    UserEntity userEntitySelectByUserName(String userName);

    @Select("select * from user where userEmail = #{userEmail}")
    UserEntity userEntitySelectByUserEmail(String userEmail);

    @Select("select * from user where userId = #{userId}")
    UserEntity userEntitySelectByUserId(long userId);

}
