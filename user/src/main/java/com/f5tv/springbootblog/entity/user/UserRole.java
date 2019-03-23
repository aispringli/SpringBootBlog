package com.f5tv.springbootblog.entity.user;


/**
 * @author 34499
 * @Title: UserRole
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 9:18 2019/3/11
 */
public class UserRole {

    public int userRoleId;

    public String userRoleName;

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }
}
