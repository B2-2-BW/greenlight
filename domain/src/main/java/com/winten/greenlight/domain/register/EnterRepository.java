package com.winten.greenlight.domain.register;

import com.winten.greenlight.domain.Customer;
import reactor.core.publisher.Mono;

public interface EnterRepository {
    /* 사용자의 입장 티켓을 만들어주는 메서드(고유ID, 순번) */
    Mono<Customer> generateTicket(Double waitingScore);

    /* 사용자의 입장 티켓을 저장해주는 메서드 */
    Mono<Customer> enrollCustomer(Customer customer);
}
