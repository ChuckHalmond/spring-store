package com.mygroup.springstore.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.exception.DataAccessException.DAESource;
import com.mygroup.springstore.manager.TokenManager;
import com.mygroup.springstore.service.UserService;
import com.mygroup.springstore.viewmodel.UserFormViewModel;

import java.util.List;
import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserAPI {
    
    private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);
    
    private final String MESSAGE_EMAIL_EXISTS = "A user with this email already exists!";
    private final String MESSAGE_CREATE_FAIL = "Impossible to create this user, please try again";
    private final String MESSAGE_CREATE_SUCCESS = "User successfully created!";
    private final String MESSAGE_UPDATE_SUCCESS = "User successfully updated!";
    private final String MESSAGE_UPDATE_FAIL = "Impossible to update this user, please try again";
    private final String MESSAGE_MAPPING_ERROR = "Unexpected mapping error!";
    private final String MESSAGE_WRONG_ID = "Given id is not valid!";
    private final String MESSAGE_WRONG_ID_FORMAT = "The id must be an integer!";
    private final String MESSAGE_WRONG_TOKEN = "Invalid token";
    private final String HIDE_PASSWORD = "******";

    @Autowired
    private UserService userService;
    
    @Autowired
    private TokenManager tokenManager;
    
    @PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPost(
            @RequestBody @Valid UserFormViewModel ufvm,
            BindingResult br) {
        
        logger.info("Entering UserAPI at /api/user/new (POST)");

        // Thymeleaf handles the basic input errors
        if (br.hasErrors()) {
            String message = String.format(
                    "Given view model has %d input errors", 
                    br.getAllErrors().size()
            );
            logger.error(message);
            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", message);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
        }

        // Try to create and login the new user
        try {
            userService.createUser(ufvm);
            logger.info("User successfully created");

            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", MESSAGE_CREATE_SUCCESS);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.OK);
        }
        // The given email (unique) might be already taken
        catch (DataAccessException e) {
            logger.error(e.getMessage());
            
            String message = null;
            if (e.getSource() == DAESource.UNIQUE_ATTR_DUP) {
                message = MESSAGE_CREATE_FAIL;
            }
            else {
                message = MESSAGE_EMAIL_EXISTS;
            }
            
            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", message);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(
            value = "/show", 
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> showGet(@RequestHeader HttpHeaders headers) {
        try {
            UserFormViewModel ufvm = userService.getSessionUserFormViewModel();
            logger.info("User successfully retrieved");
            
            List<String> tokens = headers.get("authentificationToken");

            if (tokens == null || !tokenManager.verifyToken(tokens.get(0), ufvm.getId())) {
                JSONObject JSONanswer = new JSONObject();
                JSONanswer.put("status", MESSAGE_WRONG_TOKEN);
                return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.UNAUTHORIZED);
            }
            else {
                try {
                    // Hide the password
                    ufvm.setPassword(HIDE_PASSWORD);

                    // Map the view model to json
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return new ResponseEntity<String>(
                                mapper.writeValueAsString(ufvm), 
                                HttpStatus.OK
                        );
                    }
                    catch (JsonProcessingException e) {
                        JSONObject JSONanswer = new JSONObject();
                        JSONanswer.put("status", MESSAGE_MAPPING_ERROR);
                        return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
                    }
                }
                // The given id might be not an integer
                catch (NumberFormatException e) {
                    logger.error(e.getMessage());
                    JSONObject JSONanswer = new JSONObject();
                    JSONanswer.put("status", MESSAGE_WRONG_ID_FORMAT);
                    return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
                }
            }
        }
        catch (DataAccessException e) {
            logger.error(e.getMessage());

            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", MESSAGE_WRONG_ID);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
            
        }
        catch (IllegalArgumentException e) {
            logger.error(e.getMessage());

            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", MESSAGE_WRONG_TOKEN);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
            
        }
    }

    @PostMapping(
            value = "/edit", 
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> updatePost(
            @RequestBody @Valid UserFormViewModel uvm,
            @RequestHeader HttpHeaders headers,
            BindingResult br) {
        try 
        {
            List<String> tokens = headers.get("authentificationToken");
            if (tokens == null || !tokenManager.verifyToken(tokens.get(0), uvm.getId()))
            {
                JSONObject JSONtoken = new JSONObject();
                JSONtoken.put("status", MESSAGE_WRONG_TOKEN);

                return new ResponseEntity<String>(JSONtoken.toString(), HttpStatus.UNAUTHORIZED);
            }
            else
            {
                // Thymeleaf handles the basic input errors
                if (br.hasErrors()) {
                    String message = String.format(
                            "Given view model has %d input errors", 
                            br.getAllErrors().size()
                    );
                    logger.error(message);
                    JSONObject JSONanswer = new JSONObject();
                    JSONanswer.put("status", message);
                    return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
                }

                // Try to update the user information
                try {
                    userService.updateUser(uvm);
                    logger.info("User successfully updated");

                    JSONObject JSONanswer = new JSONObject();
                    JSONanswer.put("status", MESSAGE_UPDATE_SUCCESS);
                    return new ResponseEntity<String>(
                            JSONanswer.toString(), 
                            HttpStatus.OK
                    );
                }
                // The given id might be not an integer
                catch (NumberFormatException e) {
                    logger.error(e.getMessage());
                    JSONObject JSONanswer = new JSONObject();
                    JSONanswer.put("status", MESSAGE_WRONG_ID_FORMAT);
                    return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
                }
                // The given id might be not valid
                catch (DataAccessException e) {
                    logger.error(e.getMessage());
                    JSONObject JSONanswer = new JSONObject();
                    JSONanswer.put("status", MESSAGE_WRONG_ID);
                    return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
                }
            }
        }
        catch (DataAccessException e) {
            logger.error(e.getMessage());

            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", MESSAGE_UPDATE_FAIL);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
            
        }
        catch (IllegalArgumentException e) {
            logger.error(e.getMessage());

            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", MESSAGE_WRONG_TOKEN);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
            
        }
    }
}