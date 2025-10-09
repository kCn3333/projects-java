package com.kcn.e_shop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.custom.admin-account")
@Getter
@Setter
public class AdminConfig {
    private String username;
    private String password;
    private String email = "admin@eshop.com";
}
