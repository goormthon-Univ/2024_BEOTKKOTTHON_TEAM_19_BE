package com.example.feelsun.config.errors.exception;

import com.example.feelsun.config.utils.ApiResponseBuilder;
import lombok.Getter;

@Getter
public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public ApiResponseBuilder.ApiResponse<?> body(){
        return ApiResponseBuilder.error(getMessage());
    }
}