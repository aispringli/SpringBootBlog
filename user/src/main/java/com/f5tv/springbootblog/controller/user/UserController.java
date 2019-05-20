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
import com.f5tv.springbootblog.tools.CheckStringTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.HttpResource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    RememberMeServices rememberMeServices;

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

    @Autowired
    CheckStringTool checkStringTool;

    @Autowired
    private SessionRegistry sessionRegistry;

    //处理未登陆，判断cookie是否已记住登陆,跳转到首页
    @RequestMapping("HandleSign")
    public String HandleSign(HttpServletRequest request, HttpServletResponse response){
        Authentication authenticatedUser = rememberMeServices.autoLogin(request,response);
        if(authenticatedUser!=null){
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            //request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }
        return "redirect:/";
    }

    /**
     * @param type 判断结果类型
     * @return boolean 结果
     * @Author SpringLee
     * @Description //TODO 检查邮箱是否已注册
     * @Date 2019/4/17 11:35
     * @Param * @param userEmail 邮箱地址
     **/
    @RequestMapping("CheckUserEmail")
    @ResponseBody
    public boolean CheckUserEmail(String userEmail, boolean type) {
        boolean result = false;
        if (!StringUtils.isEmpty(userEmail)) {
            if (userService.userEntitySelectByUserEmail(userEmail) != null)
                result = true;
        }
        return type == result;
    }



    @ResponseBody
    @RequestMapping("ShowUserRole")
    public List<UserRole> ShowUserRole() {
        return userRoleService.selectAllUserRole();
    }

    @RequestMapping("HandleUserLogout")
    public String HandleUserLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            logger.info("USER : " + user.getUsername() + " LOGOUT SUCCESS !  ");
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            Cookie[] cookies = request.getCookies();
            if (cookies != null)
                for (Cookie cookie : cookies) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
        }
        return "redirect:/";
    }

    //登陆
    @RequestMapping("SignIn")
    public String SignIn() {

        return "/User/SignIn";
    }


    private ResponseResult SignAuthentication(UserEntity userEntity, String rememberme, HttpServletRequest request,HttpServletResponse response) {
        try {
            logger.info(userEntity.getUsername());
            logger.info(userEntity.getPassword());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEntity.getUserEmail(),
                    userEntity.getPassword());
            logger.info(userEntity.getPassword());
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authenticatedUser = authenticationManager.authenticate(token);
            if(!StringUtils.isEmpty(rememberme)&&"true".equals(rememberme)){
                rememberme=((UserEntity)authenticatedUser.getPrincipal()).getUsername();
                ((UserEntity)authenticatedUser.getPrincipal()).setUsername(((UserEntity)authenticatedUser.getPrincipal()).getUserEmail());
                rememberMeServices.loginSuccess(request,response,authenticatedUser);
                ((UserEntity)authenticatedUser.getPrincipal()).setUsername(rememberme);
            }
           SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
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
    public ResponseResult HandleSignIn(UserEntity userEntity, String validateCode,@RequestParam(value="remember-me" ,required =false ) String rememberme, HttpServletRequest request, HttpServletResponse response) {

        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        if (!checkStringTool.CheckEmailAddress(userEntity.getUserEmail()))
            return userResultBean.userLoginResult().get(301);
        //密码的简单判断
        if (!checkStringTool.CheckStringLength(userEntity.getPassword(), 6, 20))
            return userResultBean.userLoginResult().get(401);
        if (checkStringTool.CheckStringHasSpecialChar(userEntity.getPassword()))
            return userResultBean.userLoginResult().get(402);
        //系统校验部分
        responseResult = SignAuthentication(userEntity,rememberme, request,response);
        if (!responseResult.success)return responseResult;
        else return userResultBean.userLoginResult().get(0);
    }

    //注册
    @RequestMapping("SignUp")
    public String SignUp() {

        return "/User/SignUp";
    }

    @RequestMapping("HandleSignUp")
    @ResponseBody
    public ResponseResult HandleSignUp(UserEntity userEntity, String validateCode, String emailValidateCode, HttpServletRequest request,HttpServletResponse response) {
        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        if (!checkStringTool.CheckStringLength(userEntity.getUsername(), 2, 20))
            return userResultBean.userRegisterResult().get(201);
        if (checkStringTool.CheckStringHasSpecialChar(userEntity.getUsername()))
            return userResultBean.userRegisterResult().get(202);
        if (StringUtils.isEmpty(userEntity.getUserEmail())) return userResultBean.userRegisterResult().get(301);
        if (!checkStringTool.CheckStringLength(userEntity.getPassword(), 6, 20))
            return userResultBean.userRegisterResult().get(401);
        //检查密码复杂度 无效
        if (checkStringTool.CheckStringComplexity(userEntity.getPassword(), 2))
            return userResultBean.userRegisterResult().get(403);
        if (userEntity.getUserEmail().length() > 100) return userResultBean.userRegisterResult().get(302);

        String rightEmailAddress = (String) request.getSession().getAttribute("emailAddress");
        request.getSession().setAttribute("emailAddress", null);
        String rightEmailValidateCode = String.valueOf(request.getSession().getAttribute("emailValidateCode"));
        request.getSession().setAttribute("emailValidateCode", null);
        responseResult = emailFeignClient.CheckEmailValidateCode(userEntity.getUserEmail(), emailValidateCode, rightEmailAddress, rightEmailValidateCode);
        if (!responseResult.success) return responseResult;
        if (CheckUserEmail(userEntity.getUserEmail(), true)) return userResultBean.userRegisterResult().get(304);
        userEntity.setUserRoleId(301);//普通用户
        userEntity.setUserMotto("签名是一种态度");
        userEntity.setUserLogoSrc("/file/logo/demo_user.jpg");
        String password=userEntity.getPassword();
        userService.insert(userEntity);
        userEntity.setPassword(password);;
        responseResult = SignAuthentication(userEntity,null, request,response);
        if (!responseResult.success) return responseResult;
        else return userResultBean.userRegisterResult().get(0);
    }

    @RequestMapping("SendEmailValidateCode")
    @ResponseBody
    public ResponseResult SendEmailValidateCode(String validateCode, String emailAddress, HttpServletRequest request) {
        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        if (CheckUserEmail(emailAddress, true)) return userResultBean.userRegisterResult().get(304);
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
        if (userEntity == null) return userResultBean.userResetPasswordResult().get(101);

        String[] emailAddressArray = new String[1];
        emailAddressArray[0] = userEmail;
        Map<String, Object> datas = new HashMap<>();
        datas.put("emailAddress", userEmail);
        //校验码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        datas.put("code", bCryptPasswordEncoder.encode(userEntity.getUserId() + userEntity.getUsername() + userEntity.getPassword() + userEntity.getUserEmail()));
        //return emailFeignClient.SendEmailHtml(dates);
        return emailFeignClient.SendEmailHtml(emailAddressArray, "重置密码", "User/HandleRetrievePassword", datas, null);
    }

    //找回密码
    @RequestMapping("RetrievePassword")
    public String RetrievePassword() {

        return "/User/RetrievePassword";
    }

    @RequestMapping("HandleRetrievePassword")
    @ResponseBody
    public ResponseResult HandleRetrievePassword(String validateCode, String userEmail, String password, String code, HttpServletRequest request) {
        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        responseResult = emailFeignClient.CheckEmailAddress(userEmail);
        if (!responseResult.success) return responseResult;
        if (!checkStringTool.CheckStringLength(password, 6, 20))
            return userResultBean.userRegisterResult().get(401);
        if (!checkStringTool.CheckStringComplexity(password, 2))

            return userResultBean.userRegisterResult().get(403);
        //检查邮箱是否用过
        UserEntity userEntity = userService.userEntitySelectByUserEmail(userEmail);
        if (userEntity == null) return userResultBean.userResetPasswordResult().get(101);
        //校验码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (StringUtils.isEmpty(code) || code.length() != 60)
        try {
            if (bCryptPasswordEncoder.matches(userEntity.getUserId() + userEntity.getUsername() + userEntity.getPassword() + userEntity.getUserEmail(), code))
                return userResultBean.userResetPasswordResult().get(203);
        }catch (Exception ex){
            return userResultBean.userResetPasswordResult().get(202);
        }
        userEntity.setPassword(password);
        if (userService.updatePassword(userEntity) > 0)
            return userResultBean.userResetPasswordResult().get(0);
        return userResultBean.userResetPasswordResult().get(201);
    }


    @RequestMapping("HandleUserUpdateUsername")
    @ResponseBody
    public ResponseResult HandleUserUpdateUsername(String username) {
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(username)) return new ResponseResult(-1,"没有输入昵称");
        if (username.length()<6||username.length() > 20) return new ResponseResult(-2,"昵称长度不在范围内");
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userEntity.setUsername(username);
        if(userService.updateUsername(userEntity) > 0){
            ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setUsername(username);
            return new ResponseResult(0,true,"修改成功");
        }
        return new ResponseResult(-3,"未进行任何修改");
    }


    @RequestMapping("HandleUserUpdateMotto")
    @ResponseBody
    public boolean HandleUserUpdateMotto(String userMotto) {
        if (StringUtils.isEmpty(userMotto)) return false;
        if (userMotto.length() > 50) return false;
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userEntity == null) return false;
        userEntity.setUserMotto(userMotto);
        int result = userService.updateUserMotto(userEntity);
        if (result > 0) {
            ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setUserMotto(userMotto);
        }
        return result > 0;
    }

    @RequestMapping("HandleUserUpdatePassword")
    @ResponseBody
    public ResponseResult HandleUserUpdatePassword(String password,String newPassword) {
        if (StringUtils.isEmpty(password)||StringUtils.isEmpty(newPassword)) return new ResponseResult(-1,"没有输入密码");
        if (password.length()<6||newPassword.length()<6||password.length() > 20||newPassword.length()>20)
            return new ResponseResult(-2,"密码长度不在范围内");
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userEntity=userService.userEntitySelectByUserId(userEntity.getUserId());
        if(userEntity==null)new ResponseResult(1,"用户不存在");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(password,userEntity.getPassword()))
            return new ResponseResult(2,"原密码输入不正确");
        userEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
        if(userService.updatePassword(userEntity) > 0)return new ResponseResult(0,true,"修改成功，下次请用新密码登陆");
        return new ResponseResult(-3,"未进行任何修改");
    }

    @RequestMapping("HandleUserUpdateEmail")
    @ResponseBody
    public ResponseResult HandleUserUpdateEmail(String userEmail,String validateCode,String emailValidateCode,HttpServletRequest request) {
        ResponseResult responseResult = CheckValidateCode(validateCode, request);
        if (!responseResult.success) return responseResult;
        if (StringUtils.isEmpty(userEmail)) return new ResponseResult(-1,"没有输入邮箱");
        if (userEmail.length() > 100) return new ResponseResult(-2,"邮箱地址太长");
        if (!checkStringTool.CheckEmailAddress(userEmail))
            return new ResponseResult(-3,"邮箱地址不合法");
        String rightEmailAddress = (String) request.getSession().getAttribute("emailAddress");
        request.getSession().setAttribute("emailAddress", null);
        String rightEmailValidateCode = String.valueOf(request.getSession().getAttribute("emailValidateCode"));
        request.getSession().setAttribute("emailValidateCode", null);
        responseResult = emailFeignClient.CheckEmailValidateCode(userEmail, emailValidateCode, rightEmailAddress, rightEmailValidateCode);
        if (!responseResult.success) return responseResult;
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userEntity == null) return new ResponseResult(1,"未授权登陆");
        userEntity.setUserEmail(userEmail);
        int result = userService.updateUserEmail(userEntity);
        if (result > 0) {
            ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setUserEmail(userEmail);
            return new ResponseResult(0,true,"修改成功");
        }
        return new ResponseResult(-4,"未进行任何修改");
    }

    @RequestMapping("HandleUserUpdateLogoSrc")
    @ResponseBody
    public ResponseResult HandleUserUpdateLogoSrc(String userLogoSrc) {
        if (StringUtils.isEmpty(userLogoSrc)||StringUtils.isEmpty(userLogoSrc)) return new ResponseResult(-1,"没有输入");
        if (userLogoSrc.length()>100) return new ResponseResult(-2,"地址长度太长");
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userEntity=userService.userEntitySelectByUserId(userEntity.getUserId());
        if(userEntity==null)new ResponseResult(1,"用户不存在");
        userEntity.setUserLogoSrc(userLogoSrc);
        if(userService.updateUserLogoSrc(userEntity) > 0){
            ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setUserLogoSrc(userLogoSrc);
            return new ResponseResult(0,true,"修改完成");
        }
        return new ResponseResult(-3,"未进行任何修改");
    }

    @PreAuthorize("hasAnyAuthority('超级管理员')")//此方法只允许 超级管理员 角色 访问
    @RequestMapping("HandleUpdateUserRole")
    @ResponseBody
    public ResponseResult HandleUpdateUserRole(Long userId){
        if(userId==null)return new ResponseResult(1,"非法参数,用户不存在");
        if(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserRoleId()!=101)
            return new ResponseResult(2,"权限不足");
        UserEntity userEntity=userService.userEntitySelectByUserId(userId);
        if(userEntity==null)return new ResponseResult(3,"非法参数,用户不存在");
        if(userEntity.getUserRoleId()==201)userEntity.setUserRoleId(301);
        else if(userEntity.getUserRoleId()==301)userEntity.setUserRoleId(201);
        else return new ResponseResult(4,"非法参数");
        if(userService.updateUserRoleId(userEntity)>0)return new ResponseResult(0,true,"处理成功");
        else return new ResponseResult(-1,"处理失败");
    }

    @PreAuthorize("hasAnyAuthority('管理员')")//此方法只允许 管理员 角色 访问
    @RequestMapping("HandleUpdateUserStatus")
    @ResponseBody
    public ResponseResult HandleUpdateUserStatus(Long userId){
        if(userId==null)return new ResponseResult(1,"非法参数,用户不存在");
        UserEntity userEntity=userService.userEntitySelectByUserId(userId);
        if(userEntity==null)return new ResponseResult(3,"非法参数,用户不存在");
        if(userEntity.getUserStatus()==0)userEntity.setUserStatus(-1);
        else userEntity.setUserStatus(0);
        if(userService.updateUserStatus(userEntity)>0)return new ResponseResult(0,true,"处理成功");
        else return new ResponseResult(-1,"处理失败");
    }

}
