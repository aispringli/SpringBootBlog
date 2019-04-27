package com.f5tv.springbootblog.entity.blog;

import java.util.Date;
import com.f5tv.springbootblog.entity.user.UserEntity;

/**
 * @author SpringLee
 * @Title: Category
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 10:41 2019/4/19
 */
public class CategoryEntity extends UserEntity {

    //主键id
    private long categoryId;

    //名称
    private String categoryName;

    //状态
    private int categoryStatus;

    //日期
    private Date categoryDate;

    private long blogQuantity;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(int categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public Date getCategoryDate() {
        return categoryDate;
    }

    public void setCategoryDate(Date categoryDate) {
        this.categoryDate = categoryDate;
    }

    public long getBlogQuantity() {
        return blogQuantity;
    }

    public void setBlogQuantity(long blogQuantity) {
        this.blogQuantity = blogQuantity;
    }
}
