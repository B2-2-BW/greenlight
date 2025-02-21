package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.ReadyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReadyRepositoryImpl implements ReadyRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Boolean> isReady(Customer customer) {
        return Mono.just(new CustomerZSetEntity(customer))
            .doOnNext(entity -> log.info("customerInfo: {}", entity))
            .flatMap(entity -> redisTemplate.opsForZSet().rank(entity.key(), entity.value()))
            .doOnNext(rank -> log.info("currentRank: {}", rank))
            .flatMap(rank -> {
                if (rank == null) { //rank가 0이 아니라, 이미 0값이 되어서 enum 으로 status값을 먼저 조작한다면, rank 가 아닌 status를 가져오는 방식으로 구현해야 할 수도 있음
                    return Mono.just(Boolean.FALSE);
                } else {
                    return Mono.just(Boolean.TRUE);
                }
            });
    }
}
