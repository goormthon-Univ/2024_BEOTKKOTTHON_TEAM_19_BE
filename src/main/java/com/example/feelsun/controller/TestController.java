package com.example.feelsun.controller;

import com.example.feelsun.config.auth.PrincipalUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    @Operation(summary = "토큰 테스트", description = "토큰 테스트를 진행합니다.")
    @GetMapping("/test")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> tokenTest(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(principalUserDetails.getUser());
    }
}
