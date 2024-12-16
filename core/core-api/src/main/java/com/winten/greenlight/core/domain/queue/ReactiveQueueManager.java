package com.winten.greenlight.core.domain.queue;

import com.winten.greenlight.core.support.util.TSIDGenerator;

import com.winten.greenlight.db.core.RedisDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveQueueManager {
    private final RedisDataStore dataStore;

    public Mono<Customer> pushNewCustomer() {
        String key = QueueType.WAIT.queueName();
        long id = TSIDGenerator.tsid();
        String value = key + id;
        long score = System.nanoTime();
        return dataStore.push(key, value, (double) score)
                .flatMap(added -> {
                    if (added) {
                        return Mono.just(new Customer(key, value, score));
                    }
                    else {
                        return Mono.error(new RuntimeException("Failed to line up"));
                    }
                })
                .doOnError(Mono::error);
    }

    public Mono<Long> getRank(Customer customer) {
        return dataStore.rank(customer.key(), customer.value())
                .doOnError(Mono::error);
    }

    public Mono<QueuedCustomer> getQueuedCustomer() {
        return pushNewCustomer()
                .flatMap(customer -> getRank(customer)
                        .flatMap(rank -> Mono.just(
                                new QueuedCustomer(customer.key(), customer.value(), customer.score(), rank)
                        ))
                );
    }
}
