package com.example.feelsun.controller;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    @RequestMapping("/test")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> tokenTest(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(principalUserDetails.getUser());
    }
}
