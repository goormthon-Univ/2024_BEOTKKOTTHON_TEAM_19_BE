package com.example.feelsun.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class TreePostRequest {
    @Getter
    @Setter
    public static class TreePostCreateRequest {
        @NotNull(message = "내용을 입력해주세요.")
        private String content;
        @NotNull(message = "사진은 필수입니다.")
        private String imageUrl;
    }
}
