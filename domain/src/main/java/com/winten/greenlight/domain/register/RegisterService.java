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

    public Mono<Customer> generateCustomer(String eventId) {
        return registerRepository.generateTicket()
            .doOnNext(result -> log.info("Ticket generated: {}", result))
            .map(result -> new Customer(eventId, result.customerId(), result.waitingScore(), WaitingStatus.WAITING))
            .flatMap(this::enrollCustomer)
            .doOnSuccess(customer -> log.info("Ticket saved: {}", customer))
            .switchIfEmpty(Mono.error(new CoreException(ErrorType.EXAMPLE_NOT_FOUND, "Ticket generation failed")))
            .onErrorResume(e -> {
                log.error("Error occurred while generating ticket", e);
                return Mono.error(new CoreException(ErrorType.EXAMPLE_NOT_FOUND, "오류 발생"));
            });
    }

    public Mono<Customer> enrollCustomer(Customer customer) {
        return registerRepository.enrollCustomer(customer)
            .doOnSuccess(saved -> log.info("Successfully saved ticket for customer: {}", saved))
            .doOnError(e -> log.error("Failed to save ticket", e));
    }
}
