package com.f5tv.springbootblog.mapper.user;

import com.f5tv.springbootblog.entity.user.UserEntity;
import org.apache.ibatis.annotations.*;
import org.eclipse.jetty.server.Authentication;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;

/**
 * @author 34499
 * @Title: UserMapper
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 11:01 2019/3/12
 */
@Mapper()
@Component(value = "UserMapper")
public interface UserMapper  {
    @Results(id = "userRoleResultMap", value =
            {
                    @Result(property = "userId", column = "userId"),
                    @Result(property = "username", column = "userName"),
                    @Result(property = "userRoleId", column = "userRoleId"),
                    @Result(property = "userRoleName", column = "userRoleName"),
                    @Result(property = "userEmail", column = "userEmail"),
                    @Result(property = "userStatus", column = "userStatus"),
                    @Result(property = "userDate", column = "userDate"),
                    @Result(property = "userLogoSrc", column = "userLogoSrc"),
                    @Result(property = "userMotto", column = "userMotto")
            })

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from user where userName = #{userName}")
    UserEntity userEntitySelectByUserName(String userName);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from user where userEmail = #{userEmail}")
    UserEntity userEntitySelectByUserEmail(String userEmail);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from user where userId = #{userId}")
    UserEntity userEntitySelectByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Insert("INSERT INTO user(userName, userRoleId,userEmail,userLogoSrc,userMotto) " +
            "VALUES (#{username}, #{userRoleId}, #{userEmail}, #{userLogoSrc}, #{userMotto})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "userId")
    int insert(UserEntity userEntity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Insert("update user set userName=#{username} ,userRoleId=#{userRoleId},userEmail=#{userEmail}," +
            "userLogoSrc=#{userLogoSrc},userMotto=#{userMotto} where  userId = #{userId}")
    int update(UserEntity userEntity);


}
