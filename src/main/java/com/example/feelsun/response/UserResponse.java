package com.example.feelsun.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserResponse {

    @Getter
    @Setter
    public static class UserHistoryListResponse {
        private Integer treePostId;
        private String treePostImageUrl;
        private String treePostContent;
        private LocalDateTime createdAt;

        public UserHistoryListResponse(Integer treePostId, String treePostImageUrl, String treePostContent, LocalDateTime createdAt) {
            this.treePostId = treePostId;
            this.treePostImageUrl = treePostImageUrl;
            this.treePostContent = treePostContent;
            this.createdAt = createdAt;
        }

    }

    @Getter
    @Setter
    public static class UserTreeDetailResponse {
        private Integer treePostId;
        private String treePostImageUrl;
        private String treePostContent;
        private LocalDateTime createdAt;

        public UserTreeDetailResponse(Integer treePostId, String treePostImageUrl, String treePostContent, LocalDateTime createdAt) {
            this.treePostId = treePostId;
            this.treePostImageUrl = treePostImageUrl;
            this.treePostContent = treePostContent;
            this.createdAt = createdAt;
        }
    }

    @Getter
    @Setter
    public static class UserTreeListResponse {
        private Integer userId;
        private Integer treeId;
        private String habitName;
        private String treeImageUrl;

        public UserTreeListResponse(Integer userId, Integer treeId, String habitName, String treeImageUrl) {
            this.userId = userId;
            this.treeId = treeId;
            this.habitName = habitName;
            this.treeImageUrl = treeImageUrl;
        }

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
