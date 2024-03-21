package com.example.feelsun.request;

import com.example.feelsun.domain.User;
import com.example.feelsun.domain.UserEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserRequest {

    @Getter
    @Setter
    public static class UserSignUpRequest {

        @NotNull(message = "유저 아이디는 필수 입력 값입니다.")
        @Size(min = 6, max = 20, message = "아이디는 6자 이상 20자 이하로 입력해주세요.")
        private String username;

        @NotNull(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호는 8자 이상 20자 이내이며, 문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.")
        private String password;

        @NotNull(message = "닉네임은 필수 입력 값입니다.")
        @Size(max = 8, message = "닉네임은 8자 이하로 입력해주세요.")
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
        @NotNull(message = "유저 아이디는 필수 입력 값입니다.")
        private String username;
        @NotNull(message = "비밀번호는 필수 입력 값입니다.")
        private String password;
    }

    @Getter
    @Setter
    public static class UserCheckUsernameRequest {
        @NotNull(message = "유저 아이디는 필수 입력 값입니다.")
        @Size(min = 6, max = 20, message = "아이디는 6자 이상 20자 이하로 입력해주세요.")
        private String username;
    }

    @Getter
    @Setter
    public static class UserCheckNicknameRequest {
        @NotNull(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;
    }
}
