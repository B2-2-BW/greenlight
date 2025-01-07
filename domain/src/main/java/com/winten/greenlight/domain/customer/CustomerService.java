package com.winten.greenlight.domain.customer;

import com.winten.greenlight.support.error.CoreException;
import com.winten.greenlight.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final TicketGenerator ticketGenerator;

    public Mono<Customer> find(String eventId, String customerId) {
        return customerRepository.find(eventId, customerId)
                .zipWith(Mono.just(ticketGenerator.generate(eventId, customerId)))
                .flatMap(tuple -> {
                    Customer customer = tuple.getT1();
                    EntranceTicket ticket = tuple.getT2();
                    return Mono.just(new Customer(customer.eventId(), customer.customerId(), customer.waitingScore(), ticket));
                })
                .switchIfEmpty(Mono.error(new CoreException(ErrorType.CUSTOMER_NOT_FOUND)));
    }

    public Mono<Customer> save(Customer customer) {
        return customerRepository.save(customer);
    }
}
