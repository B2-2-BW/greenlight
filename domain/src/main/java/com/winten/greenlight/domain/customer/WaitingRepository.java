package com.winten.greenlight.domain.customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WaitingRepository {
    Mono<Waiting> save(Waiting waiting);

    Mono<Boolean> remove(Waiting waiting);

    Mono<Long> findRank(Waiting waiting);

    Mono<Long> size();

    Flux<Waiting> findAll(long count);
}
