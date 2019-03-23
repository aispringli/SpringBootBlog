package com.f5tv.springbootblog.service.user;

import com.f5tv.springbootblog.mapper.user.UserRoleMapper;
import com.f5tv.springbootblog.entity.user.UserRole;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 34499
 * @Title: UserRoleService
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 10:25 2019/3/11
 */
@MapperScan(value={"com.f5tv.springbootblog.user.mapper"})
@Service
@Component(value = "UserRoleService")
public class UserRoleService {
    @Autowired
    UserRoleMapper userRoleMapper;


    //@Cacheable(value = "models", key = "#userRole.userRoleId", condition = "#userRole.userRoleName !=  '' ")
    @Cacheable(value = "userRoles")
    public List<UserRole> selectAllUserRole(){
        return userRoleMapper.selectAllUserRole();
    }

    public int insertUserRole(UserRole userRole){
        return userRoleMapper.insertUserRole(userRole);
    }
}
