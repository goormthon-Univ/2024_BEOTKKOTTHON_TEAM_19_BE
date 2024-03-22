package com.example.feelsun.config.auth;

import com.example.feelsun.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalUserLoader {
    public User getRequestUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof PrincipalUserDetails) {
            return ((PrincipalUserDetails) authentication.getPrincipal()).getUser();
        }
        throw new IllegalStateException("No authenticated user found or user is not an instance of PrincipalUserDetails");
    }
}