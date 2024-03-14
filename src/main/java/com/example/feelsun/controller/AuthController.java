package com.example.feelsun.controller;

import com.example.feelsun.config.auth.PrincipalUserDetailsService;
import com.example.feelsun.config.errors.exception.Exception401;
import com.example.feelsun.config.jwt.JwtProvider;
import com.example.feelsun.config.jwt.refreshToken.RefreshTokenService;
import com.example.feelsun.config.utils.ApiResponseBuilder;
import com.example.feelsun.domain.User;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(newAccessToken));
    }

}
