package com.f5tv.springbootblog.service.user;

import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.mapper.user.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private SessionRegistry sessionRegistry;



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


    public int insert(UserEntity userEntity){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        return userMapper.insert(userEntity);
    }

    //修改昵称
    public int updateUsername(UserEntity userEntity){
        return userMapper.updateUsername(userEntity);
    }

    //修改邮箱
    public int updateUserEmail(UserEntity userEntity){
        return userMapper.updateUserEmail(userEntity);
    }

    //修改邮箱
    public int updatePassword(UserEntity userEntity){
        return userMapper.updatePassword(userEntity);
    }
    //修改状态
    public int updateUserStatus(UserEntity userEntity){
        int result = userMapper.updateUserStatus(userEntity);
        if (result > 0) {
            removeUserSessionByUserId(userEntity.getUserId());
        }
        return result;
    }

    //修改签名
    public int updateUserMotto(UserEntity userEntity){
        return userMapper.updateUserMotto(userEntity);
    }

    //修改头像
    public int updateUserLogoSrc(UserEntity userEntity){
        return userMapper.updateUserLogoSrc(userEntity);
    }

    //修改粉丝数量
    public int updateUserFollowerQuantity(UserEntity userEntity){
        return userMapper.updateUserFollowerQuantity(userEntity);
    }

    //修改权限
    public int updateUserRoleId(UserEntity userEntity) {
        int result = userMapper.updateUserRoleId(userEntity);
        if (result > 0) {
            removeUserSessionByUserId(userEntity.getUserId());
        }
        return result;
    }

    public boolean removeUserSessionByUserId(long userId){
        List<Object> users = sessionRegistry.getAllPrincipals(); // 获取session中所有的用户信息
        for (Object principal : users) {
            if (principal instanceof UserEntity) {
                final UserEntity userEntityRemove = (UserEntity) principal;
                if (userEntityRemove.getUserId()==userId) {
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false); // false代表不包含过期session
                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }


}
