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

        @NotNull(message = "이메일은 필수 입력 값입니다.")
        private String email;
        @NotNull(message = "비밀번호는 필수 입력 값입니다.")
        private String password;
        @NotNull(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;
        @NotNull(message = "학교는 필수 입력 값입니다.")
        private String school;
        @NotNull(message = "전공은 필수 입력 값입니다.")
        private String major;
        @NotNull(message = "학년은 필수 입력 값입니다.")
        private String grade;

        public User toEntity(UserEnum userEnum) {
            return User.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .school(school)
                    .major(major)
                    .grade(grade)
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
