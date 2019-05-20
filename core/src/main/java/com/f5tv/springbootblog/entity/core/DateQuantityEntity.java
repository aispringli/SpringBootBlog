package com.f5tv.springbootblog.entity.core;

import java.util.Date;

/**
 * @author SpringLee
 * @Title: DateQuantityEntity
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 15:07 2019/5/20
 */
public class DateQuantityEntity {

    private Date time;

    private long quantity;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }


}
