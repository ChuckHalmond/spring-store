package com.mygroup.springstore.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mygroup.springstore.exception.AuthorizationException;
import com.mygroup.springstore.manager.SessionManager;
import com.mygroup.springstore.model.UserModel;

@Component
public class PromotionAuthorizationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PromotionAuthorizationInterceptor.class);
    
    @Autowired
    private SessionManager sessionManager;

    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws AuthorizationException {

        // We suppose that the session was checked before by the AuthenticationInterceptor

        UserModel um = sessionManager.getUserSession().getUser();

        // Check if the user can edit promotions
        if (um.getUserType().canEditPromotions()) {
            return true;
        }
        else {
            String errorMsg = String.format(
                    "User %d has not the right to edit promotions.",
                    um.getId()
            );
            logger.error(errorMsg);
            throw new AuthorizationException(errorMsg);
        }
    }
}

