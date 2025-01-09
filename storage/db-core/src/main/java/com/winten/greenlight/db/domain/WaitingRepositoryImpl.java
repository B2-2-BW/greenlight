package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Waiting;
import com.winten.greenlight.domain.WaitingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Waiting> save(Waiting waiting) {
        return Mono.just(WaitingZSetEntity.from(waiting))
                .doOnNext(entity -> log.info("Joining queue: {}", entity))
                .flatMap(entity -> redisTemplate.opsForZSet().add(entity.key(), entity.value(), entity.score()))
                .flatMap(success -> {
                    if (Boolean.TRUE.equals(success)) {
                        return Mono.just(waiting);
                    } else {
                        return Mono.error(new RuntimeException("Failed to add item to queue."));
                    }
                })
         ;
    }

    public Mono<Boolean> remove(Waiting waiting) {
        return Mono.just(WaitingZSetEntity.from(waiting))
                .doOnNext(entity -> log.info("Removing waiting: {}", entity))
                .flatMap(entity -> redisTemplate.opsForZSet().remove(entity.key(), entity.value()))
                .map(removedCount -> removedCount > 0);
    }

    public Mono<Long> findRank(Waiting waiting) {
        return Mono.just(WaitingZSetEntity.from(waiting))
                .doOnNext(entity -> log.info("Finding rank: {}", waiting))
                .flatMap(entity -> redisTemplate.opsForZSet().rank(entity.key(), entity.value()));
    }

    public Mono<Long> size() {
        return redisTemplate.opsForZSet().size(WaitingZSetEntity.KEY);
    }

    public Flux<Waiting> findAll(long count) {
        if (count <= 0) {
            return Flux.empty();
        }
        String key = WaitingZSetEntity.KEY;
        return redisTemplate.opsForZSet().rangeWithScores(key, Range.closed(0L, count-1))
                .doOnNext(tuple -> {
                    if (tuple.getValue() == null) {
                        // 에러 로깅
                        log.error("Null value encountered in Redis ZSet for key: {}", key);
                    }
                })
                .filter(tuple -> tuple.getValue() != null)
                .flatMap(tuple -> Mono.just(WaitingZSetEntity.zSet(key, tuple)))
                .flatMap(entity -> Mono.just(entity.toWaiting()));
    }
}
