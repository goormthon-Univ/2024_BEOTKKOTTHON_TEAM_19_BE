package com.example.feelsun.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class TreeRequest {

    @Getter
    @Setter
    public static class TreeCreateRequest {
        @NotNull(message = "습관명은 필수 입력 값입니다.")
        private String name;
    }
}
