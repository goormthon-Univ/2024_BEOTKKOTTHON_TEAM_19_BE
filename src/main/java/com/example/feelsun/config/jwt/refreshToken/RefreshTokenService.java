package com.example.feelsun.config.jwt.refreshToken;


import com.example.feelsun.config.errors.exception.Exception401;
import com.example.feelsun.config.jwt.JwtProvider;
import com.example.feelsun.domain.User;
import com.example.feelsun.repository.UserJpaRepository;
import com.example.feelsun.request.AuthRequest;
import com.example.feelsun.response.UserResponse.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Transactional
@Service
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtProvider jwtProvider;
    private final UserJpaRepository userJpaRepository;

    public RefreshTokenService(RedisTemplate<String, Object> redisTemplate, JwtProvider jwtProvider, UserJpaRepository userJpaRepository) {
        this.redisTemplate = redisTemplate;
        this.jwtProvider = jwtProvider;
        this.userJpaRepository = userJpaRepository;
    }

    // 리프래쉬 토큰 저장
    @Transactional
    public void saveRefreshToken(String identity, String refreshToken) {
        redisTemplate.opsForValue().set(identity, refreshToken, Duration.ofDays(7)); // 리프래쉬 토큰 유효 기간 7일
    }

    // 리프래쉬 토큰 삭제
    @Transactional
    public void deleteRefreshToken(String identity) {
        redisTemplate.delete(identity);
    }

    @Transactional
    public UserLoginResponseWithToken generateToken(AuthRequest.RefreshTokenRequest requestDTO) {
        String refreshToken = requestDTO.getRefreshToken();

        if (refreshToken == null) {
            throw new Exception401("리프래쉬 토큰이 존재하지 않습니다.");
        }

        // refresh token 유효성 검사
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new Exception401("유효하지 않은 리프래쉬 토큰입니다.");
        }

        String userId = jwtProvider.getIdentity(refreshToken);

        User user = userJpaRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new Exception401("유저를 찾을 수 없습니다."));

        String newAccessToken = jwtProvider.createToken(user.getId().toString(), user.getRole().toString(), user.getNickname());

        UserLoginResponse loginResponseDTO = new UserLoginResponse(user.getId(), user.getUsername(), user.getNickname());

        return new UserLoginResponseWithToken(loginResponseDTO, newAccessToken, refreshToken);
    }


}
