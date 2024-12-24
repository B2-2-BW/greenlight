package com.winten.greenlight.db.core;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import reactor.core.publisher.Flux;
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

    public Flux<ZSetOperations.TypedTuple<String>> rangeFirst(String key, long count) {
        return redisTemplate.opsForZSet().rangeWithScores(key, Range.closed(0L, count));
    }
}