package com.winten.greenlight.domain;

import reactor.core.publisher.Mono;

public interface ReadyRepository {
    Mono<Boolean> isReady(Customer customer);
}
