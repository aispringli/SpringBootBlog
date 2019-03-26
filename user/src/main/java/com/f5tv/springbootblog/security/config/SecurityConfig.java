package com.f5tv.springbootblog.security.config;

import com.f5tv.springbootblog.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Auther Spring
 * @Date 2018/11/29 11:47
 * @Description //Spring Security 的基本配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private MyUserDetailsService myUserDetailsService;


    /**
     * @Description //不重写无法 @Autowired
     * @Date  21:27
     **/
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(myUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

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
                .loginPage("/User/SignIn")
//                .successForwardUrl("/Index")
                .and()
                .csrf()
                .ignoringAntMatchers("/Email/**")
                .and()
                .authorizeRequests()
                .antMatchers("/User/HandleSignIn","/static/**","/VerificationCode/**","favicon.ico","/Email/**").permitAll();
//                .anyRequest()
//                .authenticated()
//                .and()
//                .rememberMe()
//                .tokenValiditySeconds(1209600)
//                .and()
//                .sessionManagement().maximumSessions(1).expiredUrl("/User/SignIn");
    }

}
