package com.mygroup.springstore.controller;

import java.util.ArrayList;
import java.util.HashMap;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mygroup.springstore.exception.AuthorizationException;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.exception.DataAccessException.DAESource;
import com.mygroup.springstore.service.StoreService;
import com.mygroup.springstore.viewmodel.StoreFormViewModel;
import com.mygroup.springstore.viewmodel.StoreListViewModel;

@Controller
@RequestMapping("/store")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private final HashMap<String, String> createViewContext;
    private final HashMap<String, String> updateViewContext;
    private final HashMap<String, String> showViewContext;
    
    private final String MESSAGE_VALIDATION_KEY_EXISTS = "A store with this key already exists!";
    private final String MESSAGE_NOTIFICATION_CREATE_SUCCESS = "Store successfully created!";
    private final String MESSAGE_NOTIFICATION_UPDATE_SUCCESS = "Store successfully updated!";
    private final String MESSAGE_NOTIFICATION_DELETE_SUCCESS = "Store successfully deleted!";
    
    @Autowired
    private StoreService storeService;

    public StoreController() {
        createViewContext = new HashMap<String, String>();
        createViewContext.put("context", "create");
        createViewContext.put("title", "New store");
        createViewContext.put("formAction", "new");
        createViewContext.put("formSubmitName", "Create");
        
        updateViewContext = new HashMap<String, String>();
        updateViewContext.put("context", "update");
        updateViewContext.put("title", "Edit store");
        updateViewContext.put("formAction", "edit");
        updateViewContext.put("formSubmitName", "Update");
        
        showViewContext = new HashMap<String, String>();
        showViewContext.put("context", "show");
        showViewContext.put("title", "Show store");
        showViewContext.put("formAction", "edit");
        showViewContext.put("formSubmitName", "Update");
    }

    @GetMapping("/new")
    public String createGet(@ModelAttribute("sfvm") StoreFormViewModel sfvm,
            Model m) {

        // Add the create view context to the model
        m.addAllAttributes(createViewContext);

        // Create the empty view models for the form
        sfvm = new StoreFormViewModel();
        sfvm.initializeOpeningHoursList();
        
        // Bind the from context and the view model to the model
        m.addAttribute("sfvm", sfvm);
        
        return "view/store-form";
    }
    
    @PostMapping("/new")
    public String createPost(@ModelAttribute("sfvm") @Valid StoreFormViewModel sfvm,
            BindingResult br,
            RedirectAttributes ra,
            Model m) 
            throws DataAccessException {

        // Add the create view context to the model
        m.addAllAttributes(createViewContext);
            
        // Thymeleaf handles the basic input errors
        if (br.hasErrors()) {
            logger.info(String.format("Given view model has %d input errors", br.getAllErrors().size()));
            
            return "view/store-form";
        }

        // Try to create the store
        try {
            storeService.createStore(sfvm);
            logger.info("Store successfully created");
            
            ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_CREATE_SUCCESS);
            
            return "redirect:/store/list";
        }
        // The given key (unique) might be already taken
        catch (DataAccessException e) {
            logger.error(e.getMessage());
            
            if (e.getSource() == DAESource.UNIQUE_ATTR_DUP) {
                br.addError(new FieldError("sfvm", "key", MESSAGE_VALIDATION_KEY_EXISTS));

                return "view/store-form";
            }
            
            throw e;
        }
    }
    
    @GetMapping("/edit/{id}")
    public String updateGet(@ModelAttribute("sfvm") StoreFormViewModel sfvm,
            @PathVariable int id,
            Model m)
            throws AuthorizationException, DataAccessException {

        // Add the create view context to the model
        m.addAllAttributes(updateViewContext);

        // Create the empty view models for the form
        sfvm = storeService.getStoreById(id);

        // Bind the from context and the view model to the model
        m.addAttribute("sfvm", sfvm);
        
        return "view/store-form";
    }
    
    @PostMapping("/edit")
    public String updatePost(@ModelAttribute("sfvm") @Valid StoreFormViewModel sfvm,
            BindingResult br,
            RedirectAttributes ra,
            Model m)
            throws AuthorizationException, DataAccessException {

        // Add the create view context to the model
        m.addAllAttributes(updateViewContext);
            
        // Thymeleaf handles the basic input errors
        if (br.hasErrors()) {
            logger.info(String.format("Given view model has %d input errors", br.getAllErrors().size()));
            
            return "view/store-form";
        }

        // Try to create and login the new user
        try {
            storeService.updateStore(sfvm);
            logger.info("Store successfully updated");
            
            ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_UPDATE_SUCCESS);
            
            return "redirect:/store/list";
        }
        // The given key (unique) might be already taken
        catch (DataAccessException e) {
            logger.info(e.getMessage());
            
            if (e.getSource() == DAESource.UNIQUE_ATTR_DUP) {
                br.addError(new FieldError("sfvm", "key", MESSAGE_VALIDATION_KEY_EXISTS));

                return "view/store-form";
            }
            
            throw e;
        }
    }
    
    @GetMapping("/show/{id}")
    public String showGet(@ModelAttribute("sfvm") StoreFormViewModel sfvm,
            @PathVariable int id,
            Model m)
            throws DataAccessException {

        // Add the show view context to the model
        m.addAllAttributes(showViewContext);

        // Create the empty view models for the form
        sfvm = storeService.getStoreById(id);

        // Bind the from context and the view model to the model
        m.addAttribute("sfvm", sfvm);
        
        return "view/store-form";
    }
    
    @PostMapping("/delete")
    public String deletePost(@ModelAttribute("sfvm") StoreFormViewModel sfvm,
            RedirectAttributes ra,
            Model m)
            throws AuthorizationException, DataAccessException {

        // Delete the store
        storeService.deleteStoreById(sfvm.getId());
        logger.info("Store successfully removed");
        
        // Notify the user the action was successfull
        ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_DELETE_SUCCESS);
        
        return "redirect:/store/list";
    }
    
    @GetMapping("/list")
    public String listGet(@ModelAttribute("slvmList") ArrayList<StoreListViewModel> slvmList,
            Model m) {

        // Get the list
        slvmList = storeService.geStoresList();
        
        // View model to the model
        m.addAttribute("slvmList", slvmList);
        
        return "view/store-list";
    }
}
