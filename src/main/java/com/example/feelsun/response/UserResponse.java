package com.example.feelsun.response;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {

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
        private String email;
        private String nickname;
        private String profileImage;
        private String school;
        private String major;
        private String grade;

        public UserLoginResponse(Integer id, String email, String nickname, String profileImage, String school, String major, String grade) {
            this.id = id;
            this.email = email;
            this.nickname = nickname;
            this.profileImage = profileImage;
            this.school = school;
            this.major = major;
            this.grade = grade;
        }
    }
}