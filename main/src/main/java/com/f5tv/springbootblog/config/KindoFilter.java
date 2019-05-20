package com.f5tv.springbootblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author SpringLee
 * @Title: KindoFilter
 * @ProjectName SpringBootBlog
 * @Description: //TODO
 * @date 9:49 2019/5/13
 */
@Configuration
@WebFilter
public class KindoFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURL = request.getRequestURL().toString();
        String protocol = requestURL.split("://")[0];
        if ("http".equals(protocol)) {
            requestURL = requestURL.replace("http", "https");
            response.sendRedirect(requestURL);
        }
        filterChain.doFilter(request, response);
    }
}
