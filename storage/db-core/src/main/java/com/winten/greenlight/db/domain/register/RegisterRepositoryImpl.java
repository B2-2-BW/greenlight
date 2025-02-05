package com.winten.greenlight.db.domain.register;

import com.github.f4b6a3.tsid.TsidCreator;
import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.WaitingStatus;
import com.winten.greenlight.domain.register.RegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RegisterRepositoryImpl implements RegisterRepository {

    @Override
    public Mono<Customer> generateTicket() {    //Entity 충돌방지를 위해 domain의 Object 임시로 사용
        return Mono.fromCallable(() -> {
            String customerId = TsidCreator.getTsid().toString(); // 고객 ID 생성
            double waitingScore = System.currentTimeMillis(); // 대기 점수

            return new Customer(null, customerId, waitingScore, null);
        });
    }
}
