package com.example.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry){
        /*Spring MVC configure*/
        registry.addViewController("/security/home").setViewName("security/home");
        registry.addViewController("/security").setViewName("security/home");
        registry.addViewController("/security/hello").setViewName("security/hello");
        registry.addViewController("/security/login").setViewName("security/login");
    }
}
