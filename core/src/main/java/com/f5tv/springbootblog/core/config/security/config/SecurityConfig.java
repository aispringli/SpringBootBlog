package com.f5tv.springbootblog.core.config.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Auther Spring
 * @Date 2018/11/29 11:47
 * @Description //Spring Security 的基本配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    /**
     * @Author Spring
     * @Description //配置登陆方式
     * @Date 11:51 2018/11/29
     * @Param [http]
     * @return void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.formLogin()
                .loginPage("/Home/SignIn")
                .successForwardUrl("/Home/Index")
                .and()
                .authorizeRequests()
                .antMatchers("/Home/SignIn","/static/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .rememberMe()
                .tokenValiditySeconds(1209600)
                .and()
                .sessionManagement().maximumSessions(1).expiredUrl("/Home/SignIn");
    }

}
