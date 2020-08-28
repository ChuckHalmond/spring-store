package com.mygroup.springstore.api;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mygroup.springstore.exception.BusinessException;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.manager.TokenManager;
import com.mygroup.springstore.service.LoginService;
import com.mygroup.springstore.viewmodel.LoginFormViewModel;
import com.mygroup.springstore.viewmodel.UserFormViewModel;

@RestController
@RequestMapping("/api/login")
public class LoginAPI {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginAPI.class);
    
    private final String MESSAGE_LOGIN_SUCCESS = "You logged in successfully!";
    private final String MESSAGE_LOGIN_INVALID = "Login information invalid!";
    private final String MESSAGE_LOGOUT_SUCCESS = "You logged out successfully!";
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private TokenManager tokenManager;
    
    

    @PostMapping(value = "/process", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginFormViewModel lfvm) {

        // Try to login the user
        try {
            UserFormViewModel ufvm = loginService.login(lfvm);
            String token = tokenManager.generateToken(ufvm.getId());
            
            logger.info("User logged in successfully");
            
            JSONObject JSONtoken = new JSONObject();
            JSONtoken.put("token", token);
            JSONtoken.put("status", MESSAGE_LOGIN_SUCCESS);
            
            return new ResponseEntity<String>(JSONtoken.toString(), HttpStatus.OK);
        }
        // But the login info might be wrong
        catch (DataAccessException | BusinessException e) {
            logger.error(e.getMessage());
            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", MESSAGE_LOGIN_INVALID);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout() {

        // Log out
        loginService.logout();
        logger.info("User logged out successfully");

        JSONObject JSONanswer = new JSONObject();
        JSONanswer.put("status", MESSAGE_LOGOUT_SUCCESS);
        return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.OK);
    }
}