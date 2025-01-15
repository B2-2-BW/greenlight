package com.winten.greenlight.domain;

import reactor.core.publisher.Mono;

public interface ExampleRepository {
    Mono<Boolean> save(Example example);
}
