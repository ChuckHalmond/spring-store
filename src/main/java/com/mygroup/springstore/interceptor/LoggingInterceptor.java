package com.mygroup.springstore.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        
        if (handler instanceof HandlerMethod) {
            // There are cases where this handler isn't an instance of HandlerMethod, so the cast fails.
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            String controllerName = handlerMethod.getBean().getClass().getSimpleName().toString();
            String methodName = handlerMethod.getMethod().getName();

            logger.info(String.format(
                    "Entering [%s] at %s::%s() (%s)",
                    request.getServletPath().toString(),
                    controllerName,
                    methodName,
                    request.getMethod()
            ));
        }
        
        return true;
    }
}
