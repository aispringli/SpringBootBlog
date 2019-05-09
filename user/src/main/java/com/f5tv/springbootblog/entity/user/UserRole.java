package com.f5tv.springbootblog.entity.user;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author 34499
 * @Title: UserRole
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 9:18 2019/3/11
 */
@Entity
public class UserRole {

    /**
     * 用户角色编号id int
     **/
    private int userRoleId;

    private String userRoleName;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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
