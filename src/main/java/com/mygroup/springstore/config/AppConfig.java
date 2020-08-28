package com.mygroup.springstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    DataAccessConfig.class,
    WebConfig.class
})
@PropertySource("classpath:application.properties")
public class AppConfig {
	
}