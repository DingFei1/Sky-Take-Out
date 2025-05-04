package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt token-checked interceptor
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Check jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Determines whether the Controller's method or another resource is being intercepted
        if (!(handler instanceof HandlerMethod)) {
            //The current intercept is not a dynamic method. Direct release
            return true;
        }
        return true;

        //1. Get the token from the request header
        //String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2. Verify the token
//        try {
//            log.info("Jwt check: {}", token);
//            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
//            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
//            BaseContext.setCurrentId(empId);
//            log.info("Current employee id: {}", empId);
//            //3. through, release
//            return true;
//        } catch (Exception ex) {
//            //4. If no, respond to 401 status code
//            response.setStatus(401);
//            return false;
//        }
    }
}
