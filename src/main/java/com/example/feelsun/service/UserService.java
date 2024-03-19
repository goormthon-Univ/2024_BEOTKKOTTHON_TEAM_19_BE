package com.example.feelsun.service;

import com.example.feelsun.config.errors.exception.Exception400;
import com.example.feelsun.config.jwt.JwtProvider;
import com.example.feelsun.config.jwt.refreshToken.RefreshTokenService;
import com.example.feelsun.domain.User;
import com.example.feelsun.domain.UserEnum;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.UserRequest.*;
import com.example.feelsun.response.UserResponse.*;
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
    private final JwtProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(UserSignUpRequest requestDTO) {
        // 비밀번호 암호화
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        // 저장
        userJpaRepository.save(requestDTO.toEntity(UserEnum.USER));
    }

    @Transactional
    public UserLoginResponseWithToken login(UserLoginRequest requestDTO) {
        User user = userJpaRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> new Exception400(null, "아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            throw new Exception400(null, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = JwtProvider.TOKEN_PREFIX + tokenProvider.createToken(user.getId().toString(), user.getRole().toString(), user.getNickname());

        // 리프래쉬 토큰 생성
        String refreshToken = tokenProvider.createRefreshToken(user.getId().toString());

        // 리프래쉬 토큰을 Redis에 저장
        refreshTokenService.saveRefreshToken(user.getId().toString(), refreshToken);

        UserLoginResponse loginResponseDTO = new UserLoginResponse(user.getId(), user.getUsername(), user.getNickname());

        return new UserLoginResponseWithToken(loginResponseDTO, accessToken, refreshToken);
    }
}
