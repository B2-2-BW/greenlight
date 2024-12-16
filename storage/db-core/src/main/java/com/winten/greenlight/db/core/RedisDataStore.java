package com.winten.greenlight.db.core;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

public class RedisDataStore {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RedisDataStore(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Boolean> push(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    public Mono<Boolean> pop(String key, String value) {
        return redisTemplate.opsForZSet().remove(key, value).map(removedCount -> removedCount > 0);
    }

    public Mono<Long> rank(String key, String value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }
}