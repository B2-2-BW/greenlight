package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Example;
import com.winten.greenlight.domain.ExampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ExampleRepositoryImpl implements ExampleRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Boolean> save(Example example) {
        return Mono.just(new ExampleZSetEntity(example))
                .doOnNext(entity -> log.info("saving: {}", entity))
                .flatMap(entity -> redisTemplate.opsForZSet().add(entity.key(), entity.value(), entity.score()));
    }
}
