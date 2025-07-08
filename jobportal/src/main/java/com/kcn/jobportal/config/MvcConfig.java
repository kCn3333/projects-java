package com.kcn.jobportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static final String UPLOAD_DIRECTORY="photos";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirecory(UPLOAD_DIRECTORY,registry);
    }

    private void exposeDirecory(String uploadDirectory, ResourceHandlerRegistry registry) {
        Path path= Paths.get(uploadDirectory);
        registry.addResourceHandler("/"+uploadDirectory+"/**").addResourceLocations("file:"+path.toAbsolutePath()+"/");
    }
}
