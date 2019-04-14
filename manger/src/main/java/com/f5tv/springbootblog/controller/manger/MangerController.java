package com.f5tv.springbootblog.controller.manger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SpringLee
 * @Title: MangerController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 10:13 2019/4/12
 */
@Controller
@RequestMapping("Manger")
public class MangerController {

    @RequestMapping("Index")
    public String Index(){
        return "/Manger/Index";
    }
}
