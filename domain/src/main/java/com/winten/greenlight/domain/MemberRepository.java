package com.winten.greenlight.domain;

import reactor.core.publisher.Mono;


public interface MemberRepository {
    // 특정 사용자의 대기순번 조회
    Mono<Integer> getMemberQueuePosition(Customer customer);

    // 전체 대기자 수 조회
    Mono<Integer> getTotalWaitingCount(Customer customer);
}

