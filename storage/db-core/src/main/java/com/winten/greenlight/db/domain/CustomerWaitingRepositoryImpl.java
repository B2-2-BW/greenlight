package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.CustomerWaitingRepository;
import com.winten.greenlight.domain.WaitingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomerWaitingRepositoryImpl implements CustomerWaitingRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    //고객 이탈(삭제)
    @Override
    public Mono<Boolean> deleteById(String eventId, String customerId) {
        return Mono.just(new CustomerZSetEntity(new Customer(eventId, customerId, 0.0, WaitingStatus.WAITING, null)))
            .flatMap(entity -> {
                return redisTemplate.opsForZSet().remove(entity.key(), entity.value())
                    .flatMap(removedCount -> {
                        if (removedCount > 0) { //삭제된 고객 데이터 있음 ( 삭제 성공 )
                            return Mono.just(true);
                        } else { //삭제되는 고객 데이터 없음
                            return Mono.just(false);
                        }
                    });
            });

    }
}
