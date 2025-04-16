package com.sakura.idempotent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotentService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 检查是否是重复请求
     */
    public boolean check(String key, int expireSeconds) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1",
                Duration.ofSeconds(expireSeconds));
        return result != null && result;
    }
}