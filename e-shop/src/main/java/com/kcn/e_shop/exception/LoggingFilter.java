package com.kcn.e_shop.exception;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String user = (request.getUserPrincipal() != null)
                ? request.getUserPrincipal().getName()
                : "anonymous";

        //log.info("Request [{}] from user={} ip={}", uri, user, ip);

        filterChain.doFilter(request, response);
    }
}

