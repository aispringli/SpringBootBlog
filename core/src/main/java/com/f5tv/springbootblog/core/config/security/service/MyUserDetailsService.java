package com.f5tv.springbootblog.core.config.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;


/**
 * @Auther Spring
 * @Date 2018/11/29 11:57
 * @Description //自定义用户登录校验
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger= LoggerFactory.getLogger(getClass());

//    @Autowired
//    DataUserService dataUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //logger.info("登陆用户名:"+username);
        //DataUser dataUser=dataUserService.selectByUserName(username);
//        if(dataUser==null){
//            throw new UsernameNotFoundException("用户名不存在");
//        }
        //String password=PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456");
        //logger.info("密码:"+dataUser.getPassword());
        //return new User(username,"{bcrypt}"+dataUser.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return new User("admin","123456",AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        //return new User(username,"{bcrypt}2a10$pOLMkd13n7i3DtVijLEqze1zeURpjtVz5rAx1qOAPqCQvjGG/d6D.", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        //return new User(username,"{noop}123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
