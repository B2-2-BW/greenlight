package com.winten.greenlight.db.domain;

import com.winten.greenlight.db.core.RedisDataStore;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Queue;

@Component
@RequiredArgsConstructor
public class QueueRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public Mono<QueueItem> joinQueueItem(QueueItem queueItem) {
        log.info("Joining queue: " + queueItem);
        return redisTemplate.opsForZSet().add(queueItem.queueType().name(), queueItem.member(), (double) queueItem.entryTime())
                .flatMap(success -> {
                    if (Boolean.TRUE.equals(success)) {
                        return Mono.just(queueItem);
                    } else {
                        return Mono.error(new RuntimeException("Failed to add item to queue."));
                    }
                });
    }

    public Mono<Boolean> deleteQueueItem(QueueItem queueItem) {
        return redisTemplate.opsForZSet().remove(queueItem.queueType().name(), queueItem.member())
                .map(removedCount -> removedCount > 0);
    }

    public Mono<Long> getRank(QueueItem queueItem) {
        return redisTemplate.opsForZSet().rank(queueItem.queueType().name(), queueItem.member())
                .doOnError(Mono::error);
    }

    public Flux<QueueItem> getRangedWaitingQueueItem(long count) {
        if (count <= 0) {
            return Flux.empty();
        }
        return redisTemplate.opsForZSet().rangeWithScores(QueueType.WAIT.name(), Range.closed(0L, count-1))
                .flatMap(tuple -> Mono.just(new QueueItem(QueueType.WAIT, tuple.getValue(), tuple.getScore())));
    }
}
