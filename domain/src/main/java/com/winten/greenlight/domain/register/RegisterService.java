package com.winten.greenlight.domain.register;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.WaitingStatus;
import com.winten.greenlight.support.error.CoreException;
import com.winten.greenlight.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {
    private final RegisterRepository registerRepository;
    //질문1 여기에서 redis에 저장하는 등의 로직 처리 필요 ex saveTicket 생성 후 repository에서 저장 하는 등

    public Mono<Customer> getTicket(String eventId) {
        return registerRepository.generateTicket()
            .doOnNext(result -> log.info("saved!"))
            .map(result -> new Customer(eventId, result.customerId(), result.waitingScore(), WaitingStatus.WAITING)) // eventId 변경
            .flatMap(result -> {
                if (result.customerId() != null && result.waitingStatus() != null) {
                    return Mono.just(result); //TODO 성공 메시지 등으로 반환 필요 및 레디스 내 저장기능 생성
                } else {
                    return Mono.error(new CoreException(ErrorType.EXAMPLE_NOT_FOUND, "오류 발생")); //TODO Error Type 정의 필요(충돌방지 작업 x)
                }
            });
    }

}
