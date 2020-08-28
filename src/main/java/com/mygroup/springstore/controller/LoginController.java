package com.mygroup.springstore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mygroup.springstore.exception.BusinessException;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.manager.EncryptionManager;
import com.mygroup.springstore.service.LoginService;
import com.mygroup.springstore.viewmodel.LoginFormViewModel;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    private final String MESSAGE_VALIDATION_LOGIN_INVALID = "Login information invalid!";
    
    private final String MESSAGE_NOTIFICATION_LOGIN_SUCCESS = "You logged in successfully!";
    private final String MESSAGE_NOTIFICATION_LOGOUT_SUCCESS = "You logged out successfully!";
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private EncryptionManager encryptionManager;
    
    @GetMapping
    public String index(@ModelAttribute("lfvm") LoginFormViewModel lfvm,
            Model m) {

        m.addAttribute("lfvm", new LoginFormViewModel());
        
        return "view/login-form";
    }
    
    @PostMapping
    public String login(@ModelAttribute("lfvm") @Valid LoginFormViewModel lfvm,
            BindingResult br,
            RedirectAttributes ra,
            Model m) {

        // Thymeleaf handles the basic input errors
        if (br.hasErrors()) {
            return "view/login-form";
        }
        
        // Encrypt password
        lfvm.setPassword(encryptionManager.encryptMD5(lfvm.getPassword()));

        // Try to login
        try {
            loginService.login(lfvm);
            logger.info("User logged in successfully");
            
            ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_LOGIN_SUCCESS);

            return "redirect:/";
        }
        // But the login info might be wrong
        catch (BusinessException | DataAccessException e) {
            logger.error(e.getMessage());
            
            br.addError(new FieldError("lfvm", "password", MESSAGE_VALIDATION_LOGIN_INVALID));

            return "view/login-form";
        }
    }
    
    @GetMapping("/logout")
    public String logout(RedirectAttributes ra,
            Model m) {

        // Try to logout
        loginService.logout();

        ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_LOGOUT_SUCCESS);

        return "redirect:/";
    }
}
