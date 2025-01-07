package com.winten.greenlight.core.domain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Test {

    public static void main(String[] args) {
        Mono.just("mono 1")
                .doOnNext(p -> log.info("p1: " + p))
                .switchIfEmpty(Mono.error(new RuntimeException("error 1")))
                .doOnNext(p -> log.info("p2: " + p))
                .flatMap(p -> Mono.empty())
                .doOnNext(p -> log.info("p3: " + p))
                .switchIfEmpty(Mono.error(new RuntimeException("error 2")))
                .subscribe();

    }
}
