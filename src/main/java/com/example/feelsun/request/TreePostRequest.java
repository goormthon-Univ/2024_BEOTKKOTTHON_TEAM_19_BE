package com.example.feelsun.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class TreePostRequest {
    @Getter
    @Setter
    public static class TreePostCreateRequest {
        @NotNull(message = "습관 일지 내용은 필수 입력 값입니다.")
        private String content;

        @NotNull(message = "습관 일지 이미지는 필수 입력 값입니다.")
        private String imageUrl;
    }
}
