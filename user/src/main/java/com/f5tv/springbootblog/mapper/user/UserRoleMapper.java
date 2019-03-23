package com.f5tv.springbootblog.mapper.user;

import com.f5tv.springbootblog.entity.user.UserRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 34499
 * @Title: UserRoleMapper
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 10:13 2019/3/11
 */
@Mapper
@Component(value = "UserRoleMapper")
public interface UserRoleMapper {

    @Results(id = "userRoleResultMap", value =
            {
                    @Result(property = "userRoleId", column = "userRoleId"),
                    @Result(property = "userRoleName", column = "userRoleName")
            })

    @Select({"select * from userRole"})
    List<UserRole> selectAllUserRole();

    @Insert("INSERT INTO userRole(userRoleId, userRoleName) VALUES (#{userRoleId}, #{userRoleName})")
    int insertUserRole(UserRole userRole);

}
