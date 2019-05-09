package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.CollectEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.CollectService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author SpringLee
 * @Title: CollectController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 20:30 2019/4/25
 */
@RequestMapping("Collect")
@Controller
public class CollectController {

    @Autowired
    CollectService collectService;

    @RequestMapping("HandleIsCollect")
    @ResponseBody
    public ResponseResult HandleIsCollect(Long blogId) {
        if (blogId == null || blogId < 1) return new ResponseResult(-1, "参数非法");
        CollectEntity collectEntity = new CollectEntity();
        collectEntity.setBlogId(blogId);
        collectEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        if(collectService.selectCollectByUserIdAndBlogId(collectEntity)==null)return new ResponseResult(1,"未收藏");
        else return new ResponseResult(0,true,"已收藏");
    }


    @RequestMapping("HandleCollectAdd")
    @ResponseBody
    public ResponseResult HandleCollectAdd(Long blogId) {
        if (blogId == null || blogId < 1) return new ResponseResult(-1, "参数非法");
        CollectEntity collectEntity = new CollectEntity();
        collectEntity.setBlogId(blogId);
        collectEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        return collectService.insert(collectEntity);
    }

    @RequestMapping("HandleCollectDelete")
    @ResponseBody
    public ResponseResult HandleCollectDelete(Long blogId) {
        if (blogId == null || blogId < 1) return new ResponseResult(-1, "参数非法");
        CollectEntity collectEntity = new CollectEntity();
        collectEntity.setBlogId(blogId);
        collectEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        return collectService.delete(collectEntity);
    }

    @RequestMapping("MyCollect")
    public ModelAndView MyCollect(Integer page){
        if(page==null||page<1)page=1;
        ModelAndView modelAndView=new ModelAndView("Collect/MyCollect");
        PageHelper.startPage(1,10);
        modelAndView.addObject("collectLists",collectService.selectCollectByUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId()));
        modelAndView.addObject("pageNum",collectService.selectCollectCountByUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId()));
        return modelAndView;
    }
}
