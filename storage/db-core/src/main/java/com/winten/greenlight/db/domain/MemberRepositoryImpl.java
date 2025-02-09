package com.winten.greenlight.db.domain;

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
    private static final String QUEUE_KEY = "memberQueue"; // Redis에서 대기열 키

    // 대기순번 조회
    @Override
    public Mono<Integer> getMemberQueuePosition(String memberId) {
        return redisTemplate.opsForZSet().rank(QUEUE_KEY, memberId)
            .map(Long::intValue)  // rank() 메서드는 Long 타입이므로 int로 변환
            .defaultIfEmpty(-1);  // 대기열에 없으면 -1 반환
    }

    // 전체 대기자 수 조회
    @Override
    public Mono<Integer> getTotalWaitingCount() {
        return redisTemplate.opsForZSet().size(QUEUE_KEY)
            .map(Long::intValue) // 전체 대기 인원 수를 int로 변환
            .defaultIfEmpty(0);  // 대기열에 아무도 없으면 0 반환
    }
}






