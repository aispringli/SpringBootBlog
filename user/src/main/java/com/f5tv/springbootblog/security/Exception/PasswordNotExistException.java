package com.f5tv.springbootblog.security.Exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author SpringLee
 * @Title: PasswordException
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 16:12 2019/3/19
 */
public class PasswordNotExistException extends AuthenticationException {

    public PasswordNotExistException(String msg, Throwable t) {
        super(msg, t);
    }

    public PasswordNotExistException(String msg) {
        super(msg);
    }
}
