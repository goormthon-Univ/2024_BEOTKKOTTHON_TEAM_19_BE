package com.example.feelsun.config.jwt.refreshToken;


import com.example.feelsun.config.jwt.JwtProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Transactional
@Service
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RefreshTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
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

}
