package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.customer.Customer;
import com.winten.greenlight.domain.customer.CustomerRepository;
import com.winten.greenlight.domain.customer.Waiting;
import com.winten.greenlight.domain.customer.WaitingRepository;
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
public class CustomerRepositoryImpl implements CustomerRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Customer> find(String eventId, String customerId) {
        return Mono.just(CustomerZSetEntity.from(new Customer(eventId, customerId, 0.0)))
                .doOnNext(entity -> log.info("Finding rank: {}", customer))
                .flatMap(entity -> redisTemplate.opsForZSet().rank(entity.key(), entity.value()))
                .map(rank -> new Customer(customer.eventId(), customer.customerId(), rank));
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        return Mono.just(CustomerZSetEntity.from(customer))
                .doOnNext(entity -> log.info("Joining queue: {}", entity))
                .flatMap(entity -> redisTemplate.opsForZSet().add(entity.key(), entity.value(), entity.score()))
                .flatMap(success -> {
                    if (Boolean.TRUE.equals(success)) {
                        return Mono.just(guest);
                    } else {
                        return Mono.error(new RuntimeException("Failed to add item to queue."));
                    }
                })
         ;
    }

    @Override
    public Mono<Boolean> remove(Customer guest) {
        return Mono.just(CustomerZSetEntity.from(guest))
                .doOnNext(entity -> log.info("Removing guest: {}", entity))
                .flatMap(entity -> redisTemplate.opsForZSet().remove(entity.key(), entity.value()))
                .map(removedCount -> removedCount > 0);
    }

}
