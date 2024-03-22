package com.example.feelsun.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    // key 와 value 를 저장하는 메소드
    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    // key, value, 만료시간을 함게 저장하고 싶은 경우 사용하는 메소드
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    // key 파라미터를 활용하여 value 데이터를 조회하는 메소드
    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void expireValues(String key, int timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void setHashOps(String key, Map<String, String> data) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.putAll(key, data);
    }

    @Transactional(readOnly = true)
    public String getHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ? (String) redisTemplate.opsForHash().get(key, hashKey) : "";
    }

    @Transactional
    public void deleteHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.delete(key, hashKey);
    }

    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }

    @Transactional
    public void addExcludedIds(String userId, Set<Integer> ids) {
        String key = "excludedIds:" + userId;
        redisTemplate.opsForSet().add(key, ids.stream().map(String::valueOf).distinct().toArray(String[]::new));
        redisTemplate.expire(key, 1, TimeUnit.HOURS); // 1시간 후 만료
    }

    @Transactional
    public Set<Integer> getExcludedIds(String userId) {
        String key = "excludedIds:" + userId;
        Set<Object> excludedIdObjects = redisTemplate.opsForSet().members(key);
        if (excludedIdObjects == null) {
            return Set.of();
        }
        return excludedIdObjects.stream()
                .map(object -> {
                    try {
                        return Integer.parseInt(object.toString()); // Object를 String으로 변환한 후 Long으로 파싱
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull) // 파싱에 실패한 경우(null 반환)를 필터링
                .collect(Collectors.toSet());
    }

}