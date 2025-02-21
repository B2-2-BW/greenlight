package com.winten.greenlight.domain;

import reactor.core.publisher.Mono;

public interface CustomerWaitingRepository {
    Mono<Boolean> deleteById(String eventId, String customerId);
}
