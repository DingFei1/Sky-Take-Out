package com.sky.interceptor;

import com.sky.context.BaseContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Determines whether the Controller's method or another resource is being intercepted
        if (!(handler instanceof HandlerMethod)) {
            //The current intercept is not a dynamic method. Direct release
            return true;
        }

        BaseContext.setCurrentId(1L);
        return true;
    }
}
