package com.f5tv.springbootblog.controller.user;

import com.f5tv.springbootblog.entity.user.UserRole;
import com.f5tv.springbootblog.service.user.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 34499
 * @Title: UserRoleController
 * @ProjectName SpringBootBlog
 * @Description: TODO
 * @date 17:17 2019/3/12
 */
@Controller
@RequestMapping("/UserRole")
public class UserRoleController {

    @Autowired
    UserRoleService userRoleService;

    @RequestMapping("UserRoleAdd")
    public String UserRoleAdd() {
        return "/UserRole/UserRoleAdd";
    }

    @ResponseBody
    @RequestMapping("HandleUserRoleAdd")
    public String HandleUserRoleAdd(UserRole userRole) {
        if (userRoleService.insertUserRole(userRole) > 0) System.out.println("成功");
        else System.out.println("失败");

        return "/UserRole/HandleUserRoleAdd";
    }

    @RequestMapping("HandleUserRoleAddHtml")
    public String HandleUserRoleAddHtml(UserRole userRole) {
        if (userRoleService.insertUserRole(userRole) > 0) System.out.println("成功");
        else System.out.println("失败");
        return "/UserRole/HandleUserRoleAddHtml";
    }
}
