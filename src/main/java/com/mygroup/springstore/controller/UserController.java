package com.mygroup.springstore.controller;

import java.util.HashMap;
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

import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.exception.DataAccessException.DAESource;
import com.mygroup.springstore.service.UserService;
import com.mygroup.springstore.viewmodel.UserFormViewModel;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final HashMap<String, String> createViewContext;
    private final HashMap<String, String> updateViewContext;

    private final String MESSAGE_VALIDATION_EMAIL_EXISTS = "A user with this email already exists!";
    private final String MESSAGE_NOTIFICATION_CREATE_SUCCESS = "User successfully created! You can now log in.";
    private final String MESSAGE_NOTIFICATION_UPDATE_SUCCESS = "User successfully updated!";
    
    @Autowired
    private UserService userService;

    public UserController() {
        createViewContext = new HashMap<String, String>();
        createViewContext.put("context", "create");
        createViewContext.put("title", "New account");
        createViewContext.put("formAction", "new");
        createViewContext.put("formSubmitName", "Create");
        
        updateViewContext = new HashMap<String, String>();
        updateViewContext.put("context", "update");
        updateViewContext.put("title", "Edit account");
        updateViewContext.put("formAction", "edit");
        updateViewContext.put("formSubmitName", "Update");
    }
    
    @GetMapping("/")
    public String index(Model m) {
        return "view/user-form";
    }
    
    @GetMapping("/new")
    public String createGet(@ModelAttribute("ufvm") UserFormViewModel ufvm,
            Model m) {

        // Add the create view context to the model
        m.addAllAttributes(createViewContext);

        // Create the empty view models for the form
        ufvm = new UserFormViewModel();
        
        // Bind the from context and the view model to the model
        m.addAttribute("ufvm", ufvm);

        return "view/user-form";
    }
    
    @PostMapping("/new")
    public String createPost(@ModelAttribute("ufvm") @Valid UserFormViewModel ufvm,
            BindingResult br,
            Model m,
            RedirectAttributes ra)
            throws DataAccessException {

        // Add the create view context to the model
        m.addAllAttributes(createViewContext);
            
        // Thymeleaf handles the basic input errors
        if (br.hasErrors()) {
            logger.error(String.format("Given view model has %d input errors", br.getAllErrors().size()));
            
            return "view/user-form";
        }

        // Try to create the new user
        try {
            userService.createUser(ufvm);
            logger.info("User successfully created");

            ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_CREATE_SUCCESS);

            return "redirect:/login";
        }
        // The given email (unique) might be already taken
        catch (DataAccessException e) {
            logger.error(e.getMessage());
            
            if (e.getSource() == DAESource.UNIQUE_ATTR_DUP) {
                br.addError(new FieldError("ufvm", "email", MESSAGE_VALIDATION_EMAIL_EXISTS));

                return "view/user-form";
            }
            
            throw e;
        }
    }
    
    @GetMapping("/edit")
    public String updateGet(@ModelAttribute("ufvm") UserFormViewModel ufvm, 
            Model m) {

        // Add the update view context to the model
        m.addAllAttributes(updateViewContext);
        
        // Try to get the current user view models and bind them to the view
        ufvm = userService.getSessionUserFormViewModel();
        logger.info("Current user retrieved successfully");
        m.addAttribute("ufvm", ufvm);

        return "view/user-form";
    }

    @PostMapping("/edit")
    public String updatePost(@ModelAttribute("ufvm") @Valid UserFormViewModel ufvm,
            BindingResult br,
            Model m)
            throws DataAccessException {

        // Add the update view context to the model
        m.addAllAttributes(updateViewContext);
        
        // Thymeleaf handles the basic input errors
        if (br.hasErrors()) {
            logger.error(String.format("Given view model has %d input errors", br.getAllErrors().size()));
            
            return "view/user-form";
        }
        
        // Try to update the user information and forward the request to the login controller
        try {
            userService.updateSessionUser(ufvm);
            logger.info("User successfully updated");
            
            m.addAttribute("notif", MESSAGE_NOTIFICATION_UPDATE_SUCCESS);
            
            return "view/user-form";
        }
        // The given email (unique) might be already taken
        catch (DataAccessException e) {
            logger.error(e.getMessage());
            
            if (e.getSource() == DAESource.UNIQUE_ATTR_DUP) {
                br.addError(new FieldError("ufvm", "email", MESSAGE_VALIDATION_EMAIL_EXISTS));

                return "view/user-form";
            }
            
            throw e;
        }
    }
    
    @PostMapping("/delete")
    public String deletePost(RedirectAttributes ra) throws DataAccessException {

        // Remove the current session user
        userService.removeSessionUser();
        logger.info("User successfully removed");

        return "redirect:/login/logout";
    }
}