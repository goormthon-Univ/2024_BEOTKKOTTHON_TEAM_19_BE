package com.example.feelsun.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

public class TreePostRequest {
    @Getter
    @Setter
    public static class TreePostCreateRequest {
        // TABLE 설계 시 VARCHAR(300) 이상으로 설계할 것
        @NotNull(message = "습관 일지 내용은 필수 입력 값입니다.")
        @Size(max = 150, message = "습관 일지 내용은 150자 이하로 입력해주세요.")
        private String content;

        @NotNull(message = "습관 일지 이미지는 필수 입력 값입니다.")
        private String imageUrl;
    }
}