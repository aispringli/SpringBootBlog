package com.f5tv.springbootblog.security.Exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author SpringLee
 * @Title: PassWordNotMathException
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 16:15 2019/3/19
 */
public class PasswordNotMathException extends AuthenticationException {
    public PasswordNotMathException(String msg, Throwable t) {
        super(msg, t);
    }

    public PasswordNotMathException(String msg) {
        super(msg);
    }
}
