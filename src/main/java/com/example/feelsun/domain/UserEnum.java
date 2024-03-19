package com.example.feelsun.domain;

public enum UserEnum {
    USER("사용자"),
    ADMIN("관리자");
    private final String description;

    UserEnum (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
