package com.winten.greenlight.domain.customer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class EntryScheduler {
    private final ConfigService configService;
    private final CustomerService queueService;

    @PostConstruct
    public Disposable init() {
        return Flux.interval(Duration.ofSeconds(3))
                .onBackpressureDrop()
                .flatMap(i -> configService.getBackPressure()
                                .flatMap(backPressure -> queueService.moveCustomerToEntryQueue(backPressure))
                                .onErrorResume(Mono::error)
                )
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }
}
