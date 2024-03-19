package com.example.feelsun.controller;

import com.example.feelsun.config.errors.exception.Exception401;
import com.example.feelsun.config.jwt.JwtProvider;
import com.example.feelsun.config.jwt.refreshToken.RefreshTokenResponse.*;
import com.example.feelsun.config.utils.ApiResponseBuilder;
import com.example.feelsun.domain.User;
import com.example.feelsun.repository.UserJpaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RefreshToken", description = "Refresh-Token API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final UserJpaRepository userJpaRepository;

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // 쿠키에서 리프래쉬 토큰을 찾을 수 없는 경우
    }

    @Operation(summary = "토큰 재발급", description = "토큰 재발급을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RefreshTokenResponseDTO.class)))
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookies(request);

        if (refreshToken == null || !jwtProvider.validateRefreshToken(refreshToken)) {
            throw new Exception401("리프래쉬 토큰이 유효하지 않습니다.");
        }

        String userId = jwtProvider.getIdentity(refreshToken);
        User user = userJpaRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new Exception401("유저를 찾을 수 없습니다."));

        String newAccessToken = jwtProvider.createToken(user.getId().toString(), user.getRole().toString(), user.getNickname());

        RefreshTokenResponseDTO refreshTokenResponseDTO = new RefreshTokenResponseDTO(newAccessToken);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(refreshTokenResponseDTO));
    }

}
