package com.example.feelsun.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class AuthRequest {

    @Getter
    @Setter
    public static class RefreshTokenRequest {
        @NotNull(message = "리프레시 토큰은 필수 입력 값입니다.")
        private String refreshToken;
    }
}
