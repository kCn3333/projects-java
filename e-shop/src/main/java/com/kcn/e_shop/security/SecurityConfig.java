package com.kcn.e_shop.security;

import com.kcn.e_shop.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Public URLs - accessible without authentication
    private static final String[] publicUrl = {
            "/",
            "/register",
            "/login",
            "/error/*",
            "/product/{id}",        // Individual product pages
            "/cart",                // Cart viewing
            "/cart/**",             // All cart operations
            "/categories/{name}",   // Specific category pages
            "/favicon/**",          // Favicon
            "/style.css",           // Style
            "/chat-style.css",      // Chat style
            "/chat-ai.js",          // JS for Ai chat
            "/chat-ai/**",
            "/ai-chat/**",             // AI Chat
            "/uploads/products/*",  // Product images
    };


    // /product/new?categoryId=1 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Admin-only URLs - require ADMIN role
    private static final String[] adminUrl = {
            "/admin/**",            // All admin endpoints
            "/product/new",                 // Add new product (without params)
            "/product/new*",                // Add new product (with any suffix)
            "/product/new?**",              // Add new product (with query params)
            "/product/edit/**",     // Edit product
            "/product/delete/**",   // Delete product
            "/product-form.js",     // JS for Product Image
            "/product/generate-description", // AI description generation
            "/product-form-ai.js",           // JS for AI description and features generation
            "/product/generate-features",    // AI features generation
            "/categories/add",      // Add category
            "/categories/edit",     // Edit category
            "/categories/delete/**" // Delete category
    };

    // Authenticated user URLs - require any authenticated user
    private static final String[] authenticatedUrl = {
            "/order/create",        // Create order
            "/order/confirmation",  // Order confirmation
            "/orders",              // User orders
            "/logout"               // Logout
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Consider enabling CSRF for production with proper configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(adminUrl).hasRole("ADMIN")
                        .requestMatchers(authenticatedUrl).authenticated()
                        .requestMatchers(publicUrl).permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }
}
