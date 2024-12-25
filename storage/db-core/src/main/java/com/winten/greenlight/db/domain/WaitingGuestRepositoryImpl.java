package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.customer.WaitingGuest;
import com.winten.greenlight.domain.customer.WaitingGuestRepository;
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
public class WaitingGuestRepositoryImpl implements WaitingGuestRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<WaitingGuest> save(WaitingGuest waitingGuest) {
        log.info("Joining queue: {}", waitingGuest);
        var entity = WaitingGuestEntity.from(waitingGuest);
        return redisTemplate.opsForZSet().add(entity.key(), entity.value(), entity.score())
                .flatMap(success -> {
                    if (Boolean.TRUE.equals(success)) {
                        return Mono.just(waitingGuest);
                    } else {
                        return Mono.error(new RuntimeException("Failed to add item to queue."));
                    }
                });
    }

    public Mono<Boolean> remove(WaitingGuest waitingGuest) {
        log.info("Removing queue: {}", waitingGuest);
        var entity = WaitingGuestEntity.from(waitingGuest);
        return redisTemplate.opsForZSet().remove(entity.key(), entity.value())
                .map(removedCount -> removedCount > 0);
    }

    public Mono<Long> findRank(WaitingGuest waitingGuest) {
        log.info("Finding rank: {}", waitingGuest);
        var entity = WaitingGuestEntity.from(waitingGuest);
        return redisTemplate.opsForZSet().rank(entity.key(), entity.value())
                .doOnError(Mono::error);
    }

    public Mono<Long> size(String key) {
        return redisTemplate.opsForZSet().size(key)
                .doOnError(Mono::error);
    }

    public Flux<WaitingGuest> findAll(String key, long count) {
        if (count <= 0) {
            return Flux.empty();
        }
        return redisTemplate.opsForZSet().rangeWithScores(key, Range.closed(0L, count-1))
                .flatMap(tuple -> Mono.just(WaitingGuestEntity.parse(key, tuple.getValue(), tuple.getScore())))
                .flatMap(entity -> Mono.just(entity.toWaitingGuest()));
    }
}
