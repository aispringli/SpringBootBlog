package com.f5tv.springbootblog.controller.blog;

import com.f5tv.springbootblog.entity.blog.CommentEntity;
import com.f5tv.springbootblog.entity.core.ResponseResult;
import com.f5tv.springbootblog.entity.user.UserEntity;
import com.f5tv.springbootblog.service.blog.CommentService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author SpringLee
 * @Title: CommentController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 17:09 2019/4/30
 */
@Controller
@RequestMapping("/Comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @RequestMapping("HandleCommentAdd")
    @ResponseBody
    public ResponseResult HandleCommentAdd(CommentEntity commentEntity){
        commentEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        return commentService.commentAdd(commentEntity);
    }

    @RequestMapping("HandleCommentDeleteByCommentId")
    @ResponseBody
    public ResponseResult HandleCommentDeleteByCommentId(Long commentId){
        if(commentId==null||commentId<1)return new ResponseResult(1,"参数非法，处理失败");
        return commentService.commentDeleteByCommentId(commentId,((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
    }

    @RequestMapping("SelectComment")
    public ModelAndView SelectComment(Integer page,Long blogId){
        if(page==null||page<1)page=1;
        if(blogId==null)blogId=0L;
        PageHelper.startPage(page,10);
        ModelAndView modelAndView=new ModelAndView("/Comment/CommentListsTemplate");
        CommentEntity commentEntity=new CommentEntity();
        commentEntity.setUserId(0);
        commentEntity.setBlogId(blogId);
        commentEntity.setCommentStatus(0);
        modelAndView.addObject("commentLists",commentService.selectComment(commentEntity));
        //modelAndView.addObject("pageNum",commentService.selectCommentCount(commentEntity));
        return modelAndView;
    }

    @RequestMapping("MyComment")
    public ModelAndView MyComment(Integer page){
        if(page==null||page<1)page=1;
        PageHelper.startPage(page,10);
        ModelAndView modelAndView=new ModelAndView("/Comment/MyComment");
        CommentEntity commentEntity=new CommentEntity();
        commentEntity.setUserId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        commentEntity.setCommentStatus(0);
        commentEntity.setBlogId(0);
        modelAndView.addObject("commentLists",commentService.selectComment(commentEntity));
        modelAndView.addObject("pageNum",commentService.selectCommentCount(commentEntity));
        return modelAndView;
    }

    @RequestMapping("HandleUpdateCommentStatus")
    @ResponseBody
    public ResponseResult HandleUpdateCommentStatus(Long commentId){
        if(commentId==null||commentId<1)return new ResponseResult(1,"参数非法，处理失败");
        return commentService.UpdateCommentStatus(commentId);
    }

}
