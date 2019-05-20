package com.f5tv.springbootblog.controller.blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author SpringLee
 * @Title: ErrorController
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 17:56 2019/4/26
 */
@RequestMapping("/Error")
@Controller
@ControllerAdvice(basePackageClasses = MyErrorController.class)
public class MyErrorController extends ResponseEntityExceptionHandler{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("400")
    public String Error400(HttpServletRequest request){
        return "Error/400";
    }

    @RequestMapping("404")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String Error404(HttpServletRequest request){
        return "Error/404";
    }

    @RequestMapping("500")
    public String Error500(HttpServletRequest request){
        return "Error/500";
    }

    @RequestMapping("403")
    public String Error403(HttpServletRequest request){
        return "Error/403";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleControllerException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = getStatus(request);
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("msg",ex.getMessage());
        ex.printStackTrace();
        switch (status.value()) {
            case 400:
                modelAndView.setViewName("Error/400");
            case 500:
                modelAndView.setViewName("Error/500");
            case 403:
                modelAndView.setViewName("Error/403");
            case 404:
                modelAndView.setViewName("Error/404");
            default:
                modelAndView.setViewName("Error/500");
        }
        return modelAndView;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}