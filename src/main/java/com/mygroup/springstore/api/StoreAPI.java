package com.mygroup.springstore.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.service.StoreService;
import com.mygroup.springstore.viewmodel.StoreFormViewModel;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store")
public class StoreAPI {
    private static final Logger logger = LoggerFactory.getLogger(StoreAPI.class);
     
    private final String MESSAGE_MAPPING_ERROR = "Unexpected mapping error!";
    private final String MESSAGE_INVALID_KEY = "Given key is not valid!";
    
    @Autowired
    private StoreService storeService;
     
        @GetMapping(
            value = "/get/{key}", 
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getStoreByKey(@PathVariable String key) {

        // Try to get the store information
        try {
            StoreFormViewModel svm = storeService.getStoreByKey(key);
            logger.info("Store successfully retrieved");
            
            // Map the view model to json
            try {
                ObjectMapper mapper = new ObjectMapper();
                return new ResponseEntity<String>(
                        mapper.writeValueAsString(svm), 
                        HttpStatus.OK
                );
        
            }
            catch (JsonProcessingException e) {
                logger.error(e.getMessage(), e);
                JSONObject JSONanswer = new JSONObject();
                JSONanswer.put("status", MESSAGE_MAPPING_ERROR);
                return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // The given key might be invalid
        catch (DataAccessException e) {
            logger.error(e.getMessage(), e);
            JSONObject JSONanswer = new JSONObject();
            JSONanswer.put("status", MESSAGE_INVALID_KEY);
            return new ResponseEntity<String>(JSONanswer.toString(), HttpStatus.BAD_REQUEST);
        } 
    }
}
