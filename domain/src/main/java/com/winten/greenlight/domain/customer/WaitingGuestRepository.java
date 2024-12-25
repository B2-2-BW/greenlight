package com.winten.greenlight.domain.customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WaitingGuestRepository {
    Mono<WaitingGuest> save(WaitingGuest waitingGuest);

    Mono<Boolean> remove(WaitingGuest waitingGuest);

    Mono<Long> findRank(WaitingGuest waitingGuest);

    Mono<Long> size(String customer);

    Flux<WaitingGuest> findAll(String key, long count);
}
