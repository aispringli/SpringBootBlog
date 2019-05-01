package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.StarEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author SpringLee
 * @Title: StarController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:23 2019/4/25
 */
@Controller
@RequestMapping("Star")
public class StarController {

    @Autowired
    StarService starService;


    @RequestMapping("HandleIsStar")
    @ResponseBody
    public ResponseResult HandleIsStar(Long blogId){
        if(blogId==null||blogId<1)return new ResponseResult(-1,"参数非法，处理失败");
        StarEntity starEntity=new StarEntity();
        starEntity.setBlogId(blogId);
        starEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        if(starService.selectByUserIdAndBlogId(starEntity)==null)return new ResponseResult(1,"未点赞");
        else return new ResponseResult(0,true,"已点赞");
    }



    @RequestMapping("HandleStarAdd")
    @ResponseBody
    public ResponseResult HandleStarAdd(Long blogId){
        if(blogId==null||blogId<1)return new ResponseResult(-1,"参数非法，处理失败");
        StarEntity starEntity=new StarEntity();
        starEntity.setBlogId(blogId);
        starEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        return starService.insert(starEntity);
    }

    @RequestMapping("HandleStarDelete")
    @ResponseBody
    public ResponseResult HandleStarDelete(Long blogId){
        StarEntity starEntity=new StarEntity();
        if(blogId==null||blogId<1)return new ResponseResult(-1,"参数非法，处理失败");
        starEntity.setBlogId(blogId);
        starEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        return starService.delete(starEntity);
    }
}
