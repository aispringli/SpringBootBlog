package com.f5tv.springbootblog.service.user;

import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.mapper.user.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author SpringLee
 * @Description //TODO
 * @Date 2019/3/19 12:37
 * @Param * @param null
 * @return
 **/
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;



    /**
     * @return com.f5tv.springbootblog.entity.user.UserEntity
     * @Author 34499
     * @Description //根据邮箱查找用户信息
     * @Date 12:02
     * @Param [userEmail] 邮箱地址
     **/
    public UserEntity userEntitySelectByUserEmail(String userEmail) {
        return userMapper.userEntitySelectByUserEmail(userEmail);
    }

    /**
     * @return com.f5tv.springbootblog.entity.user.UserEntity
     * @Author 34499
     * @Description //根据用户主键查找用户信息
     * @Date 12:02
     * @Param [userId] 用户表主键id
     **/
    public UserEntity userEntitySelectByUserId(long userId) {
        return userMapper.userEntitySelectByUserId(userId);
    }


    public int insert(UserEntity userEntity) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        return userMapper.insert(userEntity);
    }

    public List<UserEntity> selectAllUser(UserEntity userEntity) {
        return userMapper.selectAllUser(userEntity);
    }

    public int selectAllUserCount(UserEntity userEntity) {
        return userMapper.selectAllUserCount(userEntity);
    }

    //修改昵称
    public int updateUsername(UserEntity userEntity) {
        return userMapper.updateUsername(userEntity);
    }

    //修改邮箱
    public int updateUserEmail(UserEntity userEntity) {
        return userMapper.updateUserEmail(userEntity);
    }

    //修改密码
    public int updatePassword(UserEntity userEntity) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        return userMapper.updatePassword(userEntity);
    }

    //修改状态
    public int updateUserStatus(UserEntity userEntity) {
        return userMapper.updateUserStatus(userEntity);
    }

    //修改签名
    public int updateUserMotto(UserEntity userEntity) {
        return userMapper.updateUserMotto(userEntity);
    }

    //修改头像
    public int updateUserLogoSrc(UserEntity userEntity) {
        return userMapper.updateUserLogoSrc(userEntity);
    }

    //修改权限
    public int updateUserRoleId(UserEntity userEntity) {
        return userMapper.updateUserRoleId(userEntity);
    }


}
