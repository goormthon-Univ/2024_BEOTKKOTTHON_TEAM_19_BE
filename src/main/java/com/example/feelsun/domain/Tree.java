package com.example.feelsun.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    private boolean certification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreeEnum accessLevel;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    private Integer continuousPeriod;

    @Builder
    public Tree(String name, User user) {
        this.name = name;
        this.user = user;
        this.level = 1;
        this.imageUrl = "https://via.placeholder.com/150";
        this.experience = 1;
        this.price = 0;
        this.accessLevel = TreeEnum.FREE;
        this.startDate = LocalDateTime.now();
        this.endDate = this.startDate.plusDays(60);
        this.createdAt = LocalDateTime.now();
        this.continuousPeriod = 0;
        this.certification = false;
    }

}
