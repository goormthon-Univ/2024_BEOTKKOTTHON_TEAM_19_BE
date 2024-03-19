package com.example.feelsun.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String profileImage;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private String grade;

    @Enumerated(EnumType.STRING)
    private UserEnum role;

    @Builder
    public User(String email, String password, String nickname, String school, String major, String grade, UserEnum role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.school = school;
        this.major = major;
        this.grade = grade;
        this.role = role;
    }

    //TO-DO : 공부지수 등 게임적인 요소를 추가할 경우, User 도메인에 필요한 필드 추가

}

