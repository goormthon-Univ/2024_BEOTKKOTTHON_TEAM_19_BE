package com.example.feelsun.config.errors.exception;

import com.example.feelsun.config.utils.ApiResponseBuilder;
import lombok.Getter;

@Getter
public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }

    public ApiResponseBuilder.ApiResponse<?> body(){
        return ApiResponseBuilder.error(getMessage());
    }
}
