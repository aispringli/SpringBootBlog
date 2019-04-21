package com.f5tv.springbootblog.entity.blog;

import java.util.Date;

/**
 * @author SpringLee
 * @Title: BlogEntity
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 13:15 2019/4/21
 */
public class BlogEntity extends Category{

    //主键id
    private long blogId;

    //标题
    private String title;

    // 简介
    private String summary;

    //logo 图片地址
    private String blogLogo;

    //内容
    private String content;

    //发表日期
    private Date blogDate;

    //状态 默认 0
    private int blogStatus;

    //点星量
    private long starQuantity;

    //收藏量
    private long collectQuantity;

    //评论量
    private long commentQuantity;




}
