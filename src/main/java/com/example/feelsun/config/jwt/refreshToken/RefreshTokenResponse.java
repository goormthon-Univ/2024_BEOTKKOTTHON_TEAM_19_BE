package com.example.feelsun.config.jwt.refreshToken;

import lombok.Getter;
import lombok.Setter;

public class RefreshTokenResponse {

    @Getter
    @Setter
    public static class RefreshTokenResponseDTO {
        private String accessToken;

        public RefreshTokenResponseDTO(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
