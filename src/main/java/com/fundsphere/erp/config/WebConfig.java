package com.fundsphere.erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // =========================================
        // UPLOADED FILES / LOGOS
        // =========================================

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        "file:./uploads/",
                        "file:uploads/",
                        "file:src/main/resources/static/uploads/",
                        "classpath:/static/uploads/"
                );


        // =========================================
        // IMAGES
        // =========================================

        registry.addResourceHandler("/images/**")
                .addResourceLocations(
                        "file:src/main/resources/static/images/",
                        "classpath:/static/images/"
                );


        // =========================================
        // CSS
        // =========================================

        registry.addResourceHandler("/css/**")
                .addResourceLocations(
                        "file:src/main/resources/static/css/",
                        "classpath:/static/css/"
                );


        // =========================================
        // JS
        // =========================================

        registry.addResourceHandler("/js/**")
                .addResourceLocations(
                        "file:src/main/resources/static/js/",
                        "classpath:/static/js/"
                );
    }
}