package com.example.feelsun.response;

import com.example.feelsun.domain.TreeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class TreeResponse {
    @Getter
    @Setter
    public static class MainTreeList {
        private Integer userId;
        private Integer treeId;
        private Integer treeLevel;
        private Integer experience;
        private String habitName;
        private String treeImageUrl;
        private String imageUrl;
    }

    @Getter
    @Setter
    public static class TreeDetail {
        private Integer userId;
        private String userName;
        private Integer treeId;
        private Integer treeLevel;
        private Integer experience;
        private String habitName;
        private String treeImageUrl;
        private String imageUrl;
        private int price;
        private TreeEnum accessLevel;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
    }
}
