package com.f5tv.springbootblog.mapper.user;

import com.f5tv.springbootblog.entity.user.UserPassword;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;

/**
 * @author SpringLee
 * @Title: UserPasswordMapper
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 15:31 2019/3/19
 */
@Mapper
@Component(value = "UserPasswordMapper")
public interface UserPasswordMapper {
    @Results(id = "userPasswordResult", value =
            {
                    @Result(property = "userId", column = "userId"),
                    @Result(property = "password", column = "password")
            })

    @Select("select * from userPassword where userId = #{userId}")
    UserPassword userPasswordByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Insert("INSERT INTO userPassword(userId, password )VALUES (#{userId}, #{password})")
    int insert(@Param("userId") long userId,@Param("password") String password);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Insert("UPDATE userPassword set password = #{password} where userId = #{userId}")
    int update(@Param("userId") long userId,@Param("password") String password);
}
