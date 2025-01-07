package com.winten.greenlight.domain.customer;

import reactor.core.publisher.Mono;

public interface CustomerRepository {
    Mono<Customer> find(String eventId, String customerId);

    Mono<Customer> save(Customer customer);

    Mono<Boolean> remove(Customer customer);

}
