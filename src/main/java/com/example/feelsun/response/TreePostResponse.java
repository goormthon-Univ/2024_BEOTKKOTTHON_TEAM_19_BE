package com.example.feelsun.response;

import lombok.Getter;
import lombok.Setter;

public class TreePostResponse {

    @Getter
    @Setter
    public static class TreePostCountResponse {
        private int todayCount;
        private int totalCount;

        public TreePostCountResponse(int todayCount, int totalCount) {
            this.todayCount = todayCount;
            this.totalCount = totalCount;
        }
    }
}
