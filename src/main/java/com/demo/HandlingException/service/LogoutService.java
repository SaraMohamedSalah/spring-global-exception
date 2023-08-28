package com.demo.HandlingException.service;

import com.demo.HandlingException.config.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {
    private final JwtUtils jwtUtils;

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (jwt != null) {
                String username = this.jwtUtils.getUserNameFromJwtToken(jwt);
                System.out.println("The user " + username + " has logged out.");
                SecurityContextHolder.clearContext();
            }

        }
    }

    public LogoutService(final JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
}
