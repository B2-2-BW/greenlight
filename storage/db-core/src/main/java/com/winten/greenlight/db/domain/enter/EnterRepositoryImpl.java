package com.winten.greenlight.db.domain.enter;

import com.github.f4b6a3.tsid.TsidCreator;
import com.winten.greenlight.db.domain.CustomerZSetEntity;
import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.register.EnterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EnterRepositoryImpl implements EnterRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Customer> generateTicket(Double waitingScore) {
        return Mono.fromCallable(() -> {
            String customerId = TsidCreator.getTsid().toString(); // 고객 ID 생성

            return new Customer(null, customerId, waitingScore, null, null);
        }).doOnSuccess(ticket -> log.info("Generated ticket: {}", ticket));
    }

    @Override
    public Mono<Customer> enrollCustomer(Customer customer) {
        return Mono.just(new CustomerZSetEntity(customer))
            .doOnNext(entity -> log.info("Saving: {}", entity))
            .flatMap(entity -> redisTemplate.opsForZSet().add(entity.key(), entity.value(), entity.score())
                .thenReturn(customer))
            .doOnSuccess(saved -> log.info("Successfully saved ticket: {}", saved))
            .doOnError(e -> log.error("Failed to save ticket", e));
    }
}
