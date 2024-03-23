package com.example.feelsun.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "trees")
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int experience;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean certification = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreeEnum accessLevel;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer continuousPeriod;

    @Builder
    public Tree(String name, User user) {
        this.name = name;
        this.user = user;
        this.level = 0;
        this.imageUrl = "https://team19-bucket.s3.ap-northeast-2.amazonaws.com/4cb50b61-d7fb-44c0-a087-ed905a6ffe93.png";
        this.experience = 0;
        this.price = 0;
        this.accessLevel = TreeEnum.FREE;
        this.createdAt = LocalDateTime.now();
        this.continuousPeriod = 0;
        this.certification = false;
    }

    @Transactional
    public void updateExperience(int experience) {
        this.experience += experience;
        this.certification = true;
        this.continuousPeriod += experience;
    }

    @Transactional
    public void upgradeTree(List<TreeImage> treeImages) {
        // 경험치 기준값을 배열로 선언
        int[] experienceThresholds = new int[]{0, 1, 2, 10, 30, 50, 70, 100};

        // 현재 경험치에 맞는 레벨 찾기
        for (int i = 1; i < experienceThresholds.length; i++) {
            if (this.experience == experienceThresholds[i]) {
                // 레벨 업그레이드 및 이미지 URL 업데이트
                this.level = i; // 경험치에 따른 레벨 설정
                this.imageUrl = treeImages.get(this.level).getTreeImageUrl();
                break; // 적합한 레벨을 찾았으므로 반복 종료
            }
        }
    }
}
