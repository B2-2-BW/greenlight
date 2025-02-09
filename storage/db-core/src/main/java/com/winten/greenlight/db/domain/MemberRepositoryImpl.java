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
    private static final String QUEUE_KEY = "queue:customer"; // Redis에서 대기열 키

    //대기순번 조회
    @Override
    public Mono<Integer> getMemberQueuePosition(Customer customer) {
        return Mono.just(new MemberZSetEntity(customer))  // Customer 객체로 ZSet 엔티티 생성
            .flatMap(entity -> redisTemplate.opsForZSet().rank(entity.key(), entity.value()))
            .map(Long::intValue)  // rank() 메서드는 Long 타입이므로 int로 변환
            .defaultIfEmpty(-1);  // 대기열에 없으면 -1 반환
    }

    // 전체 대기자 수 조회
    @Override
    public Mono<Integer> getTotalWaitingCount(Customer customer) {
        return Mono.just(new MemberZSetEntity(customer))  // 임시 Customer 객체로 ZSet 엔티티 생성
            .flatMap(entity -> redisTemplate.opsForZSet().size(entity.key()))  // 대기열의 크기 조회
            .map(Long::intValue)  // 전체 대기 인원 수를 int로 변환
            .defaultIfEmpty(0);  // 대기열에 아무도 없으면 0 반환
    }
}






