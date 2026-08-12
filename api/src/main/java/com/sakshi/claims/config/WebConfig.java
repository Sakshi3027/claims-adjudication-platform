package com.sakshi.claims.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The Angular dev server (localhost:4200) and the API (localhost:8080)
 * are different origins as far as the browser is concerned, so without
 * this the browser blocks every request before it even reaches the
 * controller. In a real deployment this would be the actual frontend
 * domain instead of localhost.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
