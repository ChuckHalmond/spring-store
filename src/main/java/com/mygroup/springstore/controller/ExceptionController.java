package com.mygroup.springstore.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mygroup.springstore.exception.AuthenticationException;
import com.mygroup.springstore.exception.AuthorizationException;

@ControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);
    
    // Handles authentication exceptions
    @ExceptionHandler(AuthenticationException.class)
    public String authenticationException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception e,
            Model m) {
        
        logger.info(
                String.format(
                    "Unhandled %s : %s",
                    e.getClass().getName(),
                    e.getMessage()
                )
        );
        
        m.addAttribute("errorMsg", "Ops! You must be logged in to go there.");

        return "view/lost";
    }
    
    // Handles autorization exceptions
    @ExceptionHandler(AuthorizationException.class)
    public String authorizationException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception e,
            Model m) {
        
        logger.info(
                String.format(
                    "Unhandled %s : %s",
                    e.getClass().getName(),
                    e.getMessage()
                )
        );
        
        m.addAttribute("errorMsg", "Ops! You tried to perform an action without authorization.");

        return "view/lost";
    }
    
    // Handles others exceptions (DataAccessException, unexpected exceptions, etc.)
    @ExceptionHandler(Exception.class)
    public String exception(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception e,
            Model m) {
        
        logger.info(
                String.format(
                    "Unhandled %s : %s",
                    e.getClass().getName(),
                    e.getMessage()
                )
        );
        
        m.addAttribute("errorMsg", "Ops! Something unexpected appened.");

        return "view/lost";
    }
}
