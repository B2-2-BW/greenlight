package com.winten.greenlight.domain;

import reactor.core.publisher.Mono;

public interface CustomerWaitingRepository {
    Mono<Customer> findById(String eventId, String customerId);

    Mono<Boolean> deleteById(String eventId, String customerId);
}
