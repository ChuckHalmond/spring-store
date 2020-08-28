package com.mygroup.springstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mygroup.springstore.interceptor.AuthenticationInterceptor;
import com.mygroup.springstore.interceptor.LoggingInterceptor;
import com.mygroup.springstore.interceptor.PromotionAuthorizationInterceptor;
import com.mygroup.springstore.interceptor.StoreAuthorizationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer 
{
	@Autowired 
    public LoggingInterceptor loggingInterceptor;
    
	@Autowired 
    public AuthenticationInterceptor authenticationInterceptor;
	
	@Autowired 
    public StoreAuthorizationInterceptor storeAuthorizationInterceptor;
	
	@Autowired 
    public PromotionAuthorizationInterceptor promotionAuthorizationInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(loggingInterceptor);

        registry
            .addInterceptor(authenticationInterceptor)
            .addPathPatterns("/user/**")
            .addPathPatterns("/store/**")
            .addPathPatterns("/promotion/**")
            .excludePathPatterns("/user/new");
        
        registry
            .addInterceptor(storeAuthorizationInterceptor)
            .addPathPatterns("/store/**")
            .excludePathPatterns("/store/show/**")
            .excludePathPatterns("/store/list/**");
        
        registry
            .addInterceptor(promotionAuthorizationInterceptor)
            .addPathPatterns("/promotion/**")
            .excludePathPatterns("/promotion/show/**")
            .excludePathPatterns("/promotion/list/**");
    }
}
