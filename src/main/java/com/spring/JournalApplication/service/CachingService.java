package com.spring.JournalApplication.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class CachingService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T getFromCache(String key, TypeReference<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) return null;
            return objectMapper.readValue(value.toString(), type);
        } catch (Exception e) {
            log.error("Error retrieving from cache for key {}: {}", key, e.getMessage());
            return null;
        }
    }

    public void putToCache(String key, Object value, Duration ttl) {
        try {
            String jsonString = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonString, ttl);
        } catch (Exception e) {
            log.error("Error putting into cache.");
            throw new RuntimeException(e);
        }
    }
}
