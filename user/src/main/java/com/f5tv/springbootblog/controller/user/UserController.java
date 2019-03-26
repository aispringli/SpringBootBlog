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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private ResponseResult SignAuthentication(UserEntity userEntity, HttpServletRequest request) {
        try {
            logger.info(userEntity.getUsername());
            logger.info(userEntity.getPassword());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEntity.getUsername(),
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
            logger.error(badCredentialsException.getMessage());
            //密码错误
            return userResultBean.userLoginResult().get(403);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            //其他异常
            return commonResultBean.publicErrorResult().get(-102);
        }
        return new ResponseResult(0, true, "授权成功");
    }

    private ResponseResult CheckValidateCode(String validateCode, HttpServletRequest request) {
        ResponseResult responseResult = kaptchaFeignClient.CheckValidateCode(validateCode, (String) request.getSession().getAttribute("validateCode"));
        request.getSession().setAttribute("validateCode", "");
        return responseResult;
    }

    //登陆
    @RequestMapping("HandleSignIn")
    @ResponseBody
    public ResponseResult HandleSignIn(UserEntity userEntity, String validateCode, HttpServletRequest request) {

        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        //邮箱的判断
        if (CheckTool.checkEmailAddress(userEntity.getUsername())) {
            responseResult = emailFeignClient.CheckEmailAddress(userEntity.getUsername());
            if (!responseResult.success) return responseResult;
            userEntity.setUserEmail(userEntity.getUsername());
        } else {
            //用户名的判断
            if (StringUtils.isEmpty(userEntity.getUsername()) || userEntity.getUsername().length() < 6 || userEntity.getUsername().length() > 20) return userResultBean.userLoginResult().get(201);
            if (CheckTool.checkStringHasSpecialChar(userEntity.getUsername()))
                return userResultBean.userLoginResult().get(202);
        }
        //密码的简单判断
        if (StringUtils.isEmpty(userEntity.getPassword()) || userEntity.getPassword().length() < 6 || userEntity.getPassword().length() > 20)
            return userResultBean.userLoginResult().get(401);
        if (CheckTool.checkStringHasSpecialChar(userEntity.getPassword()))
            return userResultBean.userLoginResult().get(402);
        //系统校验部分
        responseResult = SignAuthentication(userEntity, request);
        if (!responseResult.success) return responseResult;
        else return userResultBean.userLoginResult().get(0);
    }

    //注册
    @RequestMapping("SignUp")
    public String SignUp() {

        return "/User/SignUp";
    }

    @RequestMapping("HandleSignUp")
    @ResponseBody
    public ResponseResult HandleSignUp(UserEntity userEntity, String validateCode, String emailValidateCode, HttpServletRequest request) {
        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        if (StringUtils.isEmpty(userEntity.getUsername()) || userEntity.getUsername().length() < 6 || userEntity.getUsername().length() > 20)
            return userResultBean.userRegisterResult().get(201);
        if (CheckTool.checkStringHasSpecialChar(userEntity.getUsername()))
            return userResultBean.userRegisterResult().get(202);
        if (StringUtils.isEmpty(userEntity.getUserEmail())) return userResultBean.userRegisterResult().get(301);
        if (StringUtils.isEmpty(userEntity.getPassword()) || userEntity.getPassword().length() < 6 || userEntity.getPassword().length() > 20)
            return userResultBean.userRegisterResult().get(401);
        if (userEntity.getUserEmail().length() > 100) return userResultBean.userRegisterResult().get(302);

        String rightEmailAddress = (String) request.getSession().getAttribute("emailAddress");
        request.getSession().setAttribute("emailAddress", null);
        String rightEmailValidateCode = String.valueOf(request.getSession().getAttribute("emailValidateCode"));
        request.getSession().setAttribute("emailValidateCode", null);
        responseResult = emailFeignClient.CheckEmailValidateCode(userEntity.getUserEmail(), emailValidateCode, rightEmailAddress, rightEmailValidateCode);
        if (!responseResult.success) return responseResult;
        responseResult = CheckUserNameOrUserEmail(userEntity.getUsername(), userEntity.getUserEmail());
        if (responseResult != null) return responseResult;
        userEntity.setUserRoleId(301);//普通用户
        userService.insert(userEntity);
        responseResult = SignAuthentication(userEntity, request);
        if (!responseResult.success) return responseResult;
        else return userResultBean.userRegisterResult().get(0);
    }

    @RequestMapping("SendEmailValidateCode")
    @ResponseBody
    public ResponseResult SendEmailValidateCode(String validateCode, String emailAddress, HttpServletRequest request) {
        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        responseResult = CheckUserNameOrUserEmail(null, emailAddress);
        if (responseResult != null) return responseResult;
        int emailValidateCode = (int) ((Math.random() * 9 + 1) * 100000);
        request.getSession().setAttribute("emailValidateCode", emailValidateCode);
        request.getSession().setAttribute("emailAddress", emailAddress);
        return emailFeignClient.SendEmailValidateCode(emailAddress, "注册验证码", "您好，欢迎您注册博客，本次操作验证码： {emailValidateCode}", emailValidateCode);
    }

    //找回密码
    @RequestMapping("RetrievePasswordEmail")
    public String RetrievePasswordEmail() {

        return "/User/RetrievePasswordEmail";
    }

    @RequestMapping("HandleRetrievePasswordEmail")
    @ResponseBody
    public ResponseResult HandleRetrievePasswordEmail(String validateCode, String userEmail, HttpServletRequest request) {
        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        //
        responseResult = emailFeignClient.CheckEmailAddress(userEmail);
        if (!responseResult.success) return responseResult;
        //检查邮箱是否用过
        UserEntity userEntity = userService.userEntitySelectByUserEmail(userEmail);
        if (userEntity == null) return userResultBean.userResetPasswordEmailResult().get(101);
        String[] emailAddressArray = new String[1];
        emailAddressArray[0] = userEmail;
        Map<String, Object> datas = new HashMap<>();
        datas.put("emailAddress", userEmail);
        //校验码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        datas.put("code", bCryptPasswordEncoder.encode(userEntity.getUserId() + userEntity.getUsername() + userEntity.getPassword() + userEntity.getUserEmail()));
        //return emailFeignClient.SendEmailHtml(dates);
        return emailFeignClient.SendEmailHtml(emailAddressArray, "重置密码", "/User/HandleRetrievePassword", datas, null);
    }


}
