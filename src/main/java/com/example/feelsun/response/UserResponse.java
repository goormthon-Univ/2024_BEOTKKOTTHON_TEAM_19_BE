package com.example.feelsun.response;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {

    @Getter
    @Setter
    public static class UserTreeListResponse {
        private Integer userId;
        private Integer treeId;
        private String habitName;
        private String treeImageUrl;

    }



    @Getter
    @Setter
    public static class UserLoginResponseWithToken {
        private UserLoginResponse loginResponseDTO;
        private String accessToken;
        private String refreshToken;

        public UserLoginResponseWithToken(UserLoginResponse loginResponseDTO, String accessToken, String refreshToken) {
            this.loginResponseDTO = loginResponseDTO;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Getter
    @Setter
    public static class UserLoginResponse {
        private Integer id;
        private String username;
        private String nickname;

        public UserLoginResponse(Integer id, String username, String nickname) {
            this.id = id;
            this.username = username;
            this.nickname = nickname;
        }
    }
}
