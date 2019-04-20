package com.f5tv.springbootblog.security.config;

import com.f5tv.springbootblog.security.service.MyUserDetailsService;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashSet;
import java.util.Set;

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

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Autowired
    private SessionRegistry sessionRegistry;
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
                .loginPage("/Home/Index")
                .and()
                .csrf()
                .ignoringAntMatchers("/Email/**")
                .and()
                .authorizeRequests()
                .antMatchers("/User/HandleSignIn","/User/CheckUserEmail","/User/HandleSignUp","/Home/Index","/User/SendEmailValidateCode","/static/**","/VerificationCode/**","favicon.ico","/Email/**").permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .logout()
                .logoutUrl("/User/HandleUserLogout")
                .logoutSuccessUrl("/Home/Index")
                .and()
                .rememberMe()
                .tokenValiditySeconds(1209600)
                .and()
                .sessionManagement()
                .maximumSessions(1).
                expiredUrl("/Home/Index")
                .sessionRegistry(sessionRegistry);
//                .anyRequest()
//                .authenticated()
//                .and()
//                .rememberMe()
//                .tokenValiditySeconds(1209600)
//                .and()
//                .sessionManagement().maximumSessions(1).expiredUrl("/User/SignIn");
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        //配置模板
        templateResolver.setPrefix("classpath:/template/");
        templateResolver.setSuffix(".html");
        // 使用HTML的模式，也就是支持HTML5的方式，使用data-th-*的H5写法来写thymeleaf的标签语法
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // 之前在application.properties中看到的缓存配置
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        //模板引擎增加SpringSecurityDialect，让模板能用到sec前缀，获取spring security的内容
        SpringTemplateEngine engine = new SpringTemplateEngine();
        SpringSecurityDialect securityDialect = new SpringSecurityDialect();
        Set<IDialect> dialects = new HashSet<>();
        dialects.add(securityDialect);
        LayoutDialect layoutDialect=new LayoutDialect();
        dialects.add(layoutDialect);
        engine.setAdditionalDialects(dialects);
        engine.setTemplateResolver(templateResolver());
        //允许在内容中使用spring EL表达式
        engine.setEnableSpringELCompiler(true);

        return engine;
    }

//    //声明ViewResolver
    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }

}
