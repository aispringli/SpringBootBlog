package com.f5tv.springbootblog.mapper.user;

import com.f5tv.springbootblog.entity.user.UserPassword;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

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
    @Results(id = "userRoleResultMap", value =
            {
                    @Result(property = "userId", column = "userId"),
                    @Result(property = "password", column = "password")
            })

    @Select("select * from userPassword where userId = #{userId}")
    UserPassword userPasswordByUserId(long userId);
}
