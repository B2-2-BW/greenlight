package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    // 대기순번 조회
    @Override
    public Mono<Integer> getMemberQueuePosition(Customer customer) {
//        Mono.defer() 사용:
//        지연 실행으로 필요할 때 Mono가 생성됨.
//        MemberZSetEntity를 실행 시점에 생성해 더 나은 메모리 관리.
        return Mono.defer(() -> {
            MemberZSetEntity entity = new MemberZSetEntity(customer);
            return redisTemplate.opsForZSet().rank(entity.key(), entity.value())
                .map(Long::intValue)
                .defaultIfEmpty(-1);
        });
    }

    // 전체 대기자 수 조회
    @Override
    public Mono<Integer> getTotalWaitingCount(Customer customer) {
        return Mono.defer(() -> {
            MemberZSetEntity entity = new MemberZSetEntity(customer);
            return redisTemplate.opsForZSet().size(entity.key())
                .map(Long::intValue)
                .defaultIfEmpty(0);
        });
    }
}
