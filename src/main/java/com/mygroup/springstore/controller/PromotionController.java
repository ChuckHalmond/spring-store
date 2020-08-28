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

import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.exception.DataAccessException.DAESource;
import com.mygroup.springstore.service.PromotionService;
import com.mygroup.springstore.viewmodel.PromotionFormViewModel;
import com.mygroup.springstore.viewmodel.PromotionListViewModel;

@Controller
@RequestMapping("/promotion")
public class PromotionController {

    private static final Logger logger = LoggerFactory.getLogger(PromotionController.class);

    private final HashMap<String, String> createViewContext;
    private final HashMap<String, String> updateViewContext;
    private final HashMap<String, String> showViewContext;
    
    private final String MESSAGE_VALIDATION_KEY_EXISTS = "A promotion with this key already exists!";
    private final String MESSAGE_NOTIFICATION_CREATE_SUCCESS = "Promotion successfully created!";
    private final String MESSAGE_NOTIFICATION_UPDATE_SUCCESS = "Promotion successfully updated!";
    private final String MESSAGE_NOTIFICATION_DELETE_SUCCESS = "Promotion successfully deleted!";
    
    @Autowired
    private PromotionService promotionService;

    public PromotionController() {
        createViewContext = new HashMap<String, String>();
        createViewContext.put("context", "create");
        createViewContext.put("title", "New promotion");
        createViewContext.put("formAction", "new");
        createViewContext.put("formSubmitName", "Create");
        
        updateViewContext = new HashMap<String, String>();
        updateViewContext.put("context", "update");
        updateViewContext.put("title", "Edit promotion");
        updateViewContext.put("formAction", "edit");
        updateViewContext.put("formSubmitName", "Update");
        
        showViewContext = new HashMap<String, String>();
        showViewContext.put("context", "show");
        showViewContext.put("title", "Show promotion");
        showViewContext.put("formAction", "edit");
        showViewContext.put("formSubmitName", "Update");
    }

    @GetMapping("/new")
    public String createGet(@ModelAttribute("pfvm") PromotionFormViewModel pfvm,
            Model m) {

        // Add the create view context to the model
        m.addAllAttributes(createViewContext);

        // Create the empty view models for the form
        pfvm = new PromotionFormViewModel();
        
        // Bind the from context and the view model to the model
        m.addAttribute("pfvm", pfvm);
        
        return "view/promotion-form";
    }
    
    @PostMapping("/new")
    public String createPost(@ModelAttribute("pfvm") @Valid PromotionFormViewModel pfvm,
            BindingResult br,
            RedirectAttributes ra,
            Model m)
            throws DataAccessException {

        // Add the create view context to the model
        m.addAllAttributes(createViewContext);
            
        // Thymeleaf handles the basic input errors
        if (br.hasErrors()) {
            logger.info(String.format("Given view model has %d input errors", br.getAllErrors().size()));
            
            return "view/promotion-form";
        }

        try {
            promotionService.createPromotion(pfvm);
            logger.info("Promotion successfully created");
            
            ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_CREATE_SUCCESS);
            
            return "redirect:/promotion/list";
        }
        // The given key (unique) might be already taken
        catch (DataAccessException e) {
            logger.error(e.getMessage());
            
            if (e.getSource() == DAESource.UNIQUE_ATTR_DUP) {
                br.addError(new FieldError("pfvm", "key", MESSAGE_VALIDATION_KEY_EXISTS));

                return "view/promotion-form";
            }
            
            throw e;
        }
    }
    
    @GetMapping("/edit/{id}")
    public String updateGet(@ModelAttribute("sfvm") PromotionFormViewModel pfvm,
            @PathVariable int id,
            Model m)
            throws DataAccessException {

        // Add the create view context to the model
        m.addAllAttributes(updateViewContext);

        // Create the empty view models for the form
        pfvm = promotionService.getPromotionById(id);

        // Bind the from context and the view model to the model
        m.addAttribute("pfvm", pfvm);
        
        return "view/promotion-form";
    }
    
    @PostMapping("/edit")
    public String updatePost(@ModelAttribute("pfvm") @Valid PromotionFormViewModel pfvm,
            BindingResult br,
            RedirectAttributes ra,
            Model m)
            throws DataAccessException {

        // Add the create view context to the model
        m.addAllAttributes(updateViewContext);
            
        // Thymeleaf handles the basic input errors
        if (br.hasErrors()) {
            logger.info(String.format("Given view model has %d input errors", br.getAllErrors().size()));
            
            return "view/promotion-form";
        }

        // Try to create and login the new user
        try {
            promotionService.updatePromotion(pfvm);
            logger.info("Promotion successfully updated");
            
            ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_UPDATE_SUCCESS);
            
            return "redirect:/promotion/list";
        }
        // The given key (unique) might be already taken
        catch (DataAccessException e) {
            logger.info(e.getMessage());
            
            br.addError(new FieldError("pfvm", "key", MESSAGE_VALIDATION_KEY_EXISTS));
            
            return "view/promotion-form";
        }
    }
    
    @GetMapping("/show/{id}")
    public String showGet(@ModelAttribute("pfvm") PromotionFormViewModel pfvm,
            @PathVariable int id,
            Model m)
            throws DataAccessException {

        // Add the show view context to the model
        m.addAllAttributes(showViewContext);

        // Create the empty view models for the form
        pfvm = promotionService.getPromotionById(id);

        // Bind the from context and the view model to the model
        m.addAttribute("pfvm", pfvm);
        
        return "view/promotion-form";
    }
    
    @PostMapping("/delete")
    public String deletePost(@ModelAttribute("pfvm") PromotionFormViewModel pfvm,
            Model m,
            RedirectAttributes ra)
            throws DataAccessException {

        // Delete the store
        promotionService.deletePromotionById(pfvm.getId());
        logger.info("Promotion successfully removed");
        
        // Notify the user the action was successfull
        ra.addFlashAttribute("notif", MESSAGE_NOTIFICATION_DELETE_SUCCESS);

        return "redirect:/promotion/list";
    }
    
    @GetMapping("/list")
    public String listGet(@ModelAttribute("plvmList") ArrayList<PromotionListViewModel> plvmList,
            Model m) {

        // Get the list
        plvmList = promotionService.getPromotionsList();
        
        // View model to the model
        m.addAttribute("plvmList", plvmList);
        
        return "view/promotion-list";
    }
}