package com.f5tv.springbootblog.controller.user;

import com.f5tv.springbootblog.config.beans.CommonResultBean;
import com.f5tv.springbootblog.config.user.configbeans.UserResultBean;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.entity.user.UserRole;
import com.f5tv.springbootblog.feign.clients.core.KaptchaFeignClient;
import com.f5tv.springbootblog.feign.clients.email.EmailFeignClient;
import com.f5tv.springbootblog.service.user.UserRoleService;
import com.f5tv.springbootblog.service.user.UserService;
import com.f5tv.springbootblog.tools.CheckTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 34499
 * @Title: UserController
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 10:27 2019/3/11
 */
@Controller
@RequestMapping("/User")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserResultBean userResultBean;

    @Autowired
    CommonResultBean commonResultBean;

    @Autowired
    UserService userService;

    @Autowired
    KaptchaFeignClient kaptchaFeignClient;

    @Autowired
    EmailFeignClient emailFeignClient;
    /**
     * @param userEmail 邮箱
     * @return com.f5tv.springbootblog.entity.core.ResponseResult
     * @Author SpringLee
     * @Description //TODO 检查用户名或邮箱是否可用
     * @Date 2019/3/20 21:02
     * @Param * @param userName 用户名
     **/
    @RequestMapping("CheckUserNameOrUserEmail")
    @ResponseBody
    public ResponseResult CheckUserNameOrUserEmail(String userName, String userEmail) {
        if (!StringUtils.isEmpty(userName)) {
            if (userService.userEntitySelectByUserName(userName) != null)
                return userResultBean.userRegisterResult().get(501);
        }
        if (!StringUtils.isEmpty(userEmail)) {
            if (userService.userEntitySelectByUserEmail(userEmail) != null)
                return userResultBean.userRegisterResult().get(502);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("ShowUserRole")
    public List<UserRole> ShowUserRole() {
        return userRoleService.selectAllUserRole();
    }

    //登陆
    @RequestMapping("SignIn")
    public String SignIn() {

        return "/User/SignIn";
    }

    //登陆
    @RequestMapping("TestSignIn")
    @ResponseBody
    public UserEntity TestSignIn() {

        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        logger.info(userEntity.toString());
        return userEntity;
    }

    //登陆
    @RequestMapping("HandleSignIn")
    @ResponseBody
    public ResponseResult HandleSignIn(UserEntity userEntity, String validateCode, HttpServletRequest request) {
        ResponseResult responseResult = kaptchaFeignClient.checkValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        //邮箱的判断
        if (CheckTool.checkEmailAddress(userEntity.getUserName())) {
            responseResult=emailFeignClient.checkEmailAddress(userEntity.getUserName());
            if(!responseResult.success)return responseResult;
            userEntity.setUserEmail(userEntity.getUserName());
        } else {
            //用户名的判断
            if (userEntity.getUserName().length() > 20) return userResultBean.userLoginResult().get(201);
            if (CheckTool.checkStringHasSpecialChar(userEntity.getUserName()))
                return userResultBean.userLoginResult().get(202);
        }
        //密码的简单判断
        if (StringUtils.isEmpty(userEntity.getPassword()) || userEntity.getPassword().length() < 6 || userEntity.getPassword().length() > 20)
            return userResultBean.userLoginResult().get(401);
        if (CheckTool.checkStringHasSpecialChar(userEntity.getPassword()))
            return userResultBean.userLoginResult().get(402);
        //系统校验部分
        try {
            logger.info(userEntity.getUserName());
            logger.info(userEntity.getPassword());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEntity.getUserName(),
                    userEntity.getPassword());
            logger.info(userEntity.getPassword());
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authenticatedUser = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        } catch (UsernameNotFoundException usernameNotFoundException) {
            //用户名不存在
            if (userEntity.getUserEmail() != null) return userResultBean.userLoginResult().get(303);
            else return userResultBean.userLoginResult().get(203);
        } catch (DisabledException disabledException) {
            //账户被禁用
            return userResultBean.userLoginResult().get(501);
        } catch (BadCredentialsException badCredentialsException) {
            //密码错误
            return userResultBean.userLoginResult().get(403);
        } catch (Exception ex) {
            //其他异常
            return commonResultBean.publicErrorResult().get(-102);
        }
        return userResultBean.userLoginResult().get(0);
    }

    //注册
    @RequestMapping("SignUp")
    public String SignUp() {

        return "/User/SignUp";
    }

    //找回密码
    @RequestMapping("RetrievePassword")
    public String RetrievePassword(UserEntity user) {
        if (user != null) System.out.println(user.getUserName());
        return "/User/RetrievePassword";
    }


}
