package com.example.feelsun.domain;

public enum TreeEnum {
    FREE("무료"),
    PAY("유료");
    private final String description;

    TreeEnum (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
