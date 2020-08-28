/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygroup.springstore.session;

import org.springframework.stereotype.Component;

import com.mygroup.springstore.model.UserModel;
import com.mygroup.springstore.viewmodel.UserType;

@Component
public class UserSession {
    private UserModel user;
    
    public UserSession() {
        this.user = null;
    }
    
    public UserModel getUser() {
        return user;
    }
    
    public void setUser(UserModel user) {
        this.user = user;
    }
    
    public boolean isValid() {
        return user != null;
    }

    public String getUserFirstName() {
        if (isValid()) {
            return user.getFirstName();
        }
        else {
            return "guest";
        }
    }

    public UserType getUserType() {
        if (isValid()) {
            return user.getUserType();
        }
        else {
            return null;
        }
    }
    
    public int getUserId() {
        if (isValid()) {
            return user.getId();
        }
        else {
            return 0;
        }
    }
}
