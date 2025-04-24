package com.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Value("${DEVELOP_FRONT_ADDRESS}")
    String front_address;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(front_address)
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .exposedHeaders("location")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
