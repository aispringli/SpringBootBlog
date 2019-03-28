package com.f5tv.springbootblog.service.user;

import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.entity.user.UserPassword;
import com.f5tv.springbootblog.mapper.user.UserMapper;
import com.f5tv.springbootblog.mapper.user.UserPasswordMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Author SpringLee
 * @Description //TODO
 * @Date 2019/3/19 12:37 
 * @Param  * @param null
 * @return 
 **/
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserPasswordMapper userPasswordMapper;

    /**
     * @Author 34499
     * @Description //根据用户名查找用户信息
     * @Date  12:01
     * @Param [userName] 用户名
     * @return com.f5tv.springbootblog.entity.user.UserEntity
     **/
    public UserEntity userEntitySelectByUserName(String userName){
        return userMapper.userEntitySelectByUserName(userName);
    }

    /**
     * @Author 34499
     * @Description //根据邮箱查找用户信息
     * @Date  12:02
     * @Param [userEmail] 邮箱地址
     * @return com.f5tv.springbootblog.entity.user.UserEntity
     **/
    public UserEntity userEntitySelectByUserEmail(String userEmail){
        return userMapper.userEntitySelectByUserEmail(userEmail);
    }

    /**
     * @Author 34499
     * @Description //根据用户主键查找用户信息
     * @Date  12:02
     * @Param [userId] 用户表主键id
     * @return com.f5tv.springbootblog.entity.user.UserEntity
     **/
    public UserEntity userEntitySelectByUserId(long userId){
        return userMapper.userEntitySelectByUserId(userId);
    }

    public UserPassword userPasswordSelectByUserId(long userId){
        return userPasswordMapper.userPasswordByUserId(userId);
    }

    public int insert(UserEntity userEntity){
        userMapper.insert(userEntity);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return userPasswordMapper.insert(userEntity.getUserId(),bCryptPasswordEncoder.encode(userEntity.getPassword()));
    }

    // 修改用户信息
    public int updateUser(UserEntity userEntity){
        return userMapper.update(userEntity);
    }

    // 修改用户密码
    public int updatePassword(@Param("userId") long userId, @Param("password") String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return userPasswordMapper.update(userId,bCryptPasswordEncoder.encode(password));
    }
}
