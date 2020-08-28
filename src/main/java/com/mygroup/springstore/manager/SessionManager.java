/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygroup.springstore.manager;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mygroup.springstore.session.UserSession;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionManager {

    private UserSession userSession;

    public SessionManager() {
        userSession = new UserSession();
    }
    
    public boolean sessionIsValid() {
        return userSession.isValid();
    }
    
    public UserSession getUserSession() {
        return userSession;
    }

    public void invalidateSession() {
        getSession().invalidate();
    }
    
    private HttpSession getSession() {
        ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        return sra.getRequest().getSession(true);
    }
}
