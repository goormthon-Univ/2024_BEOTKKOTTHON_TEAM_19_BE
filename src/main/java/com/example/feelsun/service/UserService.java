package com.example.feelsun.service;

import com.example.feelsun.domain.UserEnum;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.UserRequest.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;

    public void signup(UserSignUpRequest requestDTO) {
        // 비밀번호 암호화
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // 저장
        userJpaRepository.save(requestDTO.toEntity(UserEnum.USER));
    }
}
