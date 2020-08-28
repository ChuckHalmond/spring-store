package com.mygroup.springstore.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mygroup.springstore.exception.AuthenticationException;
import com.mygroup.springstore.manager.SessionManager;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
    
    @Autowired
    private SessionManager sessionManager;

    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws AuthenticationException {

        if (sessionManager.sessionIsValid()) {
            return true;
        }
        else { 
            if (handler instanceof HandlerMethod) {
                // There are cases where this handler isn't an instance of HandlerMethod, so the cast fails.
                HandlerMethod handlerMethod = (HandlerMethod)handler;
                String controllerName = handlerMethod.getBean().getClass().getSimpleName().toString();
                String methodName = handlerMethod.getMethod().getName();

                logger.debug(String.format(
                        "Authentication failed entering [%s] at %s::%s() (%s)",
                        request.getServletPath().toString(),
                        controllerName,
                        methodName,
                        request.getMethod()
                ));
            }
            
            String errorMsg = "User was not authenticated and cannot perform this action.";
            logger.error(errorMsg);
            throw new AuthenticationException(errorMsg);
        }
    }
}
