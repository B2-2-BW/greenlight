package com.winten.greenlight.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadyService {
    private final ReadyRepository readyRepository;

    public Mono<Boolean> isReady(Customer customer) {
        return readyRepository.isReady(customer)
            .doOnNext(result -> log.info("checkReady!"));
    }
}
