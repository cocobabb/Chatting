package com.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    @Value("${DEVELOP_FRONT_ADDRESS}")
    String front_address;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(front_address)
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
