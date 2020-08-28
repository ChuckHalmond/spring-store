package com.mygroup.springstore.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygroup.springstore.service.PromotionService;
import com.mygroup.springstore.viewmodel.PromotionFormViewModel;

import java.util.ArrayList;
import javax.json.Json;
import static javax.json.Json.createArrayBuilder;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotion")
public class PromotionAPI 
{
    private static final Logger logger = LoggerFactory.getLogger(PromotionAPI.class);
     
    private final String MESSAGE_MAPPING_ERROR = "Unexpected mapping error!";
    
    @Autowired
    private PromotionService promotionService;
     
    @GetMapping(
        value = "/", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getPromotions() 
    {
        // Try to get the store information
        try 
        {
            ArrayList<PromotionFormViewModel> pvms = promotionService.getPromotions();
            logger.info("Promotions successfully retrieved");
            
             JsonObject JSONResult;
             
            if(pvms.isEmpty())
            {
                JSONResult= Json.createObjectBuilder()
               .add("Promotions", "[]")
               .build();
            }
            else
            {
                JsonArrayBuilder jsonArrayPromotionsBuilder = createArrayBuilder();

                ObjectMapper mapper = new ObjectMapper();

                for(PromotionFormViewModel pvm : pvms)
                {
                    String sPromotion = mapper.writeValueAsString(pvm);
                    jsonArrayPromotionsBuilder.add(sPromotion);
                }

               JSONResult = Json.createObjectBuilder()
               .add("Promotions", jsonArrayPromotionsBuilder)
               .build();
            }
            // Map the view model to json
            return new ResponseEntity<String>(
                    JSONResult.toString(), 
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
}

