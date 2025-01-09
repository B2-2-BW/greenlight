package com.winten.greenlight.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventServiceWithCache {

    @Cacheable("eventId")
    public Mono<Event> findEventByEventId(String eventId) {
        return Mono.just(new Event()); // TODO : eventId로 event 가져오도록 코딩하기
    }
}
