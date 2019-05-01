package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.FollowEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.FollowService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author SpringLee
 * @Title: FollowController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 15:01 2019/4/25
 */
@Controller
@RequestMapping("Follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @RequestMapping("CheckUserIsFollowByUserId")
    @ResponseBody
    public ResponseResult CheckUserIsFollowByUserId(Long userId){
        if(userId==null||userId<1)return new ResponseResult(-1,"参数非法,处理失败");
        FollowEntity followEntity=new FollowEntity();
        long userFollowId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        followEntity.setUserFollowId(userFollowId);
        followEntity.setUserId(userId);
        if(followService.CheckUserIsFollowByUserId(followEntity))return new ResponseResult(0,true,"已关注");
        else return new ResponseResult(1,false,"未关注");
    }

    @RequestMapping("UserFollowByUserId")
    @ResponseBody
    public ResponseResult UserFollowByUserId(Long userId){
        if(userId==null||userId<1)return new ResponseResult(-1,"参数非法,处理失败");
        FollowEntity followEntity=new FollowEntity();
        long userFollowId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        followEntity.setUserFollowId(userFollowId);
        followEntity.setUserId(userId);
        return followService.followInsert(followEntity);
    }

    @RequestMapping("UserDeleteFollowByUserId")
    @ResponseBody
    public ResponseResult UserDeleteFollowByUserId(Long userId){
        if(userId==null||userId<1)return new ResponseResult(-1,"参数非法,处理失败");
        FollowEntity followEntity=new FollowEntity();
        long userFollowId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        followEntity.setUserFollowId(userFollowId);
        followEntity.setUserId(userId);
        return followService.followDelete(followEntity);
    }

    @RequestMapping("MyFollow")
    public ModelAndView MyFollow(Integer page){
        if(page==null||page<1)page=1;
        PageHelper.startPage(page,10);
        long userFollowId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        ModelAndView modelAndView=new ModelAndView("/Follow/MyFollow");
        modelAndView.addObject("followEntityList",followService.selectUserFollowByUserFollowId(userFollowId));
        modelAndView.addObject("pageNum",followService.selectCountUserFollowByUserFollowId(userFollowId));
        return modelAndView;
    }
    @RequestMapping("MyFun")
    public ModelAndView MyFun(Integer page){
        if(page==null||page<1)page=1;
        ModelAndView modelAndView=new ModelAndView("/Follow/MyFun");
        PageHelper.startPage(page,10);
        long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        modelAndView.addObject("followEntityList",followService.selectFollowersByUserId(userId));
        modelAndView.addObject("pageNum",followService.selectCountFollowersByUserId(userId));
        return modelAndView;
    }




}
