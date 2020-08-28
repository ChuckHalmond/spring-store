/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygroup.springstore.manager;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlManager {

    @Autowired
    private HttpServletRequest request;
    
    public String relativeUrl() {
        return request.getServletPath();
    }
    
    public boolean relativeUrlEquals(String url) {
        return request.getServletPath().equals(url);
    }
    
    public boolean relativeUrlStartsWith(String url) {
        return request.getServletPath().startsWith(url);
    }
}
