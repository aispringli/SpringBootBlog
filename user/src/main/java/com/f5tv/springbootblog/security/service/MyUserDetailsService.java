package com.f5tv.springbootblog.security.service;

import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.entity.user.UserRole;
import com.f5tv.springbootblog.security.Exception.PasswordNotExistException;
import com.f5tv.springbootblog.security.Exception.PasswordNotMathException;
import com.f5tv.springbootblog.service.user.UserRoleService;
import com.f5tv.springbootblog.service.user.UserService;
import com.f5tv.springbootblog.tools.CheckStringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @Auther Spring
 * @Date 2018/11/29 11:57
 * @Description //自定义用户登录校验
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @Autowired
    CheckStringTool checkStringTool;
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException, PasswordNotExistException, PasswordNotMathException {

        logger.info("登陆邮箱:" + userEmail);
        UserEntity userEntity;
        userEntity = userService.userEntitySelectByUserEmail(userEmail);
        if (userEntity == null) throw new UsernameNotFoundException("邮箱没有注册");

        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        if (userEntity.getUserStatus() == 0) userEntity.setEnabled(true);
        else userEntity.setEnabled(false);
        userEntity.setAuthorities(userRoleAuthorities(userEntity.getUserRoleId(),userEntity));
        return userEntity;
    }

    /**
     * @return java.util.Collection<?extends org.springframework.security.core.GrantedAuthority>
     * @Author 34499
     * @Description //根据用户权限的id判断该用户有何权限
     * @Date 10:59
     * @Param [userRoleId]
     **/
    public Collection<? extends GrantedAuthority> userRoleAuthorities(int userRoleId,UserEntity userEntity) {
        List<UserRole> userRoleList = userRoleService.selectAllUserRole();
        ArrayList<String> rolesArrayList = new ArrayList<>();
        for (int i = 0; i < userRoleList.size(); i++){
            if (userRoleList.get(i).getUserRoleId() >= userRoleId) rolesArrayList.add(userRoleList.get(i).getUserRoleName());
            if (userRoleList.get(i).getUserRoleId() == userRoleId)userEntity.setUserRoleName(userRoleList.get(i).getUserRoleName());
        }
        return AuthorityUtils.createAuthorityList(rolesArrayList.toArray(new String[rolesArrayList.size()]));
    }
}
