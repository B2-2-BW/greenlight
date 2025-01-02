package com.winten.greenlight.domain.customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GuestRepository {
    Mono<Guest> save(Guest guest);

    Mono<Boolean> remove(Guest guest);

    Mono<Long> findRank(Guest guest);

    Mono<Long> size(String customer);

    Flux<Guest> findAll(String key, long count);
}
