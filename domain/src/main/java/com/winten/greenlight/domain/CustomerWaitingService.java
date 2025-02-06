package com.winten.greenlight.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerWaitingService {
    private final CustomerWaitingRepository customerWaitingRepository;

    //고객 조회 Service
    public Mono<Customer> getCustomer(String eventId, String customerId) {
        return customerWaitingRepository.findById(eventId, customerId);
    }

    //고객 이탈(삭제) Service
    public Mono<Boolean> deleteCustomer(String eventId, String customerId) {
        return customerWaitingRepository.deleteById(eventId, customerId);
    }
}
