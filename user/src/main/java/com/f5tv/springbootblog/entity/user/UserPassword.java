package com.f5tv.springbootblog.entity.user;

/**
 * @author 34499
 * @Title: UserPassword
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 9:47 2019/3/11
 */
public class UserPassword {



    /**
     * 用户密码 String
     **/
    private String password;

    /**
     * 用户唯一id long
     **/
    private long userId;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
