package com.example.feelsun.request;

import com.example.feelsun.domain.User;
import com.example.feelsun.domain.UserEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class UserRequest {

    @Getter
    @Setter
    public static class UserSignUpRequest {

        @NotNull(message = "유저 아이디는 필수 입력 값입니다.")
        private String username;
        @NotNull(message = "비밀번호는 필수 입력 값입니다.")
        private String password;
        @NotNull(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;

        public User toEntity(UserEnum userEnum) {
            return User.builder()
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .role(userEnum)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UserLoginRequest {
        @NotNull(message = "이메일은 필수 입력 값입니다.")
        private String email;
        @NotNull(message = "비밀번호는 필수 입력 값입니다.")
        private String password;
    }
}
