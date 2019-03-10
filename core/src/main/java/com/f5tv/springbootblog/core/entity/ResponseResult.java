package com.f5tv.springbootblog.core.entity;

/**
 * @author 34499
 * @Title: ResponseResult
 * @ProjectName SpringBootBlog
 * @Description: 操作结果实体类
 * @date 12:31 2019/3/7
 */
public class ResponseResult {

    //操作状态码
    public int code;

    //是否操作成功
    public boolean success = false;

    //操作提示信息
    public String message;

    public ResponseResult() {
        this.code = -1;
        this.message = "操作失败";
    }

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(int code, boolean success, String message) {
        this.code = code;
        this.success = success;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
