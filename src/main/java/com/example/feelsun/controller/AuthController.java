package com.example.feelsun.controller;

import com.example.feelsun.config.errors.exception.Exception401;
import com.example.feelsun.config.jwt.JwtProvider;
import com.example.feelsun.config.jwt.refreshToken.RefreshTokenResponse;
import com.example.feelsun.config.jwt.refreshToken.RefreshTokenService;
import com.example.feelsun.config.utils.ApiResponseBuilder;
import com.example.feelsun.domain.User;
import com.example.feelsun.request.AuthRequest.*;
import com.example.feelsun.response.UserResponse.*;
import com.example.feelsun.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RefreshToken", description = "Refresh-Token API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "토큰 재발급", description = "토큰 재발급을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RefreshTokenResponse.class)))
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest requestDTO, Errors errors) {

        UserLoginResponseWithToken tokenDTO = refreshTokenService.generateToken(requestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(tokenDTO));
    }

}
