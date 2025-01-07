package com.winten.greenlight.domain.customer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntryScheduler {
    private final AdminConfig adminConfig;

    @PostConstruct
    public Disposable init() {
        return Flux.interval(Duration.ofSeconds(3))
                .onBackpressureDrop()
                .flatMap(i -> Mono.just(adminConfig.getBackPressure()))
                .doOnNext(backPressure -> log.info("Scheduler running with backpressure {}", backPressure))
//                .flatMap(queueService::moveFirstNCustomerToEntryQueue) //TODO implement scheduler logic
                .onErrorResume(Mono::error)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }
}
