package com.example.feelsun.controller;

import com.example.feelsun.config.errors.exception.Exception401;
import com.example.feelsun.config.jwt.JwtProvider;
import com.example.feelsun.config.jwt.refreshToken.RefreshTokenResponse;
import com.example.feelsun.config.utils.ApiResponseBuilder;
import com.example.feelsun.domain.User;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.AuthRequest.*;
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
    private final UserJpaRepository userJpaRepository;

    @Operation(summary = "토큰 재발급", description = "토큰 재발급을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RefreshTokenResponse.class)))
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest requestDTO, Errors errors) {
        String refreshToken = requestDTO.getRefreshToken();

        if (refreshToken == null) {
            throw new Exception401("리프래쉬 토큰이 존재하지 않습니다.");
        }

        // refresh token 유효성 검사
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new Exception401("유효하지 않은 리프래쉬 토큰입니다.");
        }

        String userId = jwtProvider.getIdentity(refreshToken);
        User user = userJpaRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new Exception401("유저를 찾을 수 없습니다."));

        String newAccessToken = jwtProvider.createToken(user.getId().toString(), user.getRole().toString(), user.getNickname());

        RefreshTokenResponse refreshTokenResponseDTO = new RefreshTokenResponse(newAccessToken);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(refreshTokenResponseDTO));
    }

}
