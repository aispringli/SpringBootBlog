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
                    @Result(property = "password", column = "password"),
                    @Result(property = "userRoleId", column = "userRoleId"),
                    @Result(property = "userRoleName", column = "userRoleName"),
                    @Result(property = "userEmail", column = "userEmail"),
                    @Result(property = "userStatus", column = "userStatus"),
                    @Result(property = "userDate", column = "userDate"),
                    @Result(property = "userLogoSrc", column = "userLogoSrc"),
                    @Result(property = "userMotto", column = "userMotto"),
                    @Result(property = "userFollowerQuantity", column = "userFollowerQuantity"),
            })

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from user where userEmail = #{userEmail}")
    UserEntity userEntitySelectByUserEmail(String userEmail);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Select("select * from user where userId = #{userId}")
    UserEntity userEntitySelectByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Insert("INSERT INTO user(username,password, userRoleId,userEmail,userLogoSrc,userMotto) " +
            "VALUES (#{username},#{password}, #{userRoleId}, #{userEmail}, #{userLogoSrc}, #{userMotto})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "userId")
    int insert(UserEntity userEntity);

    //修改 分几种情况

    //修改昵称
    @Update("update user set username=#{username} where userId = #{userId}")
    int updateUsername(UserEntity userEntity);

    //修改邮箱
    @Update("update user set userEmail=#{userEmail} where userId = #{userId}")
    int updateUserEmail(UserEntity userEntity);

    //修改邮箱
    @Update("update user set password=#{password} where userId = #{userId}")
    int updatePassword(UserEntity userEntity);

    //修改状态
    @Update("update user set userStatus=#{userStatus} where userId = #{userId}")
    int updateUserStatus(UserEntity userEntity);

    //修改签名
    @Update("update user set userMotto=#{userMotto} where userId = #{userId}")
    int updateUserMotto(UserEntity userEntity);

    //修改头像
    @Update("update user set userLogoSrc=#{userLogoSrc} where userId = #{userId}")
    int updateUserLogoSrc(UserEntity userEntity);

    //修改粉丝数量
    @Update("update user set userFollowerQuantity=#{userFollowerQuantity} where userId = #{userId}")
    int updateUserFollowerQuantity(UserEntity userEntity);

    //修改权限
    @Update("update user set userRoleId=#{userRoleId} where userId = #{userId}")
    int updateUserRoleId(UserEntity userEntity);


}
