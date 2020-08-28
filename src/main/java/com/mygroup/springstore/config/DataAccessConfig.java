package com.mygroup.springstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:dataaccess.properties")
public class DataAccessConfig {
	
}
