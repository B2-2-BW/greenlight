package com.winten.greenlight.core.domain;

import com.winten.greenlight.core.support.error.CoreException;
import com.winten.greenlight.core.support.error.ErrorType;
import com.winten.greenlight.core.support.util.TSIDGenerator;
import com.winten.greenlight.db.domain.*;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final TSIDGenerator tsidGenerator;
    private final QueueRepository queueRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public Mono<Customer> registerCustomerInWaitingQueue(String eventId) {
        String customerId = tsidGenerator.generate();
        Customer customer = new Customer(eventId, customerId);
        return queueRepository.joinQueueItem(customer.toWaitingQueueItem())
                .flatMap(queueItem -> Mono.just(customer))
                .onErrorResume(Mono::error);
    }

    public Mono<PollingResult> getPollingResultOfCustomer(Customer customer) {
        return queueRepository.getRank(customer.toWaitingQueueItem())
                .flatMap(rank -> Mono.just(new PollingResult(rank, null)))
                .switchIfEmpty(checkEntryQueueAndCreateTicket(customer))
                .doOnError(Mono::error);
    }

    public Mono<PollingResult> checkEntryQueueAndCreateTicket(Customer customer) {
        return queueRepository.getRank(customer.toEntryQueueItem())
                .flatMap(rank -> Mono.just(new PollingResult(0, createTicket())))
                .switchIfEmpty(Mono.error(CoreException.of(ErrorType.CUSTOMER_NOT_FOUND)));
    }

    private String createTicket() {
        String ticket = "token=1:0J9KKTN5C0GQ5&redirectUrl=https://www.thehyundai.com/front/bda/BDALiveBrodViewer.thd?pLiveBfmtNo=202411130001";
        return URLEncoder.encode(ticket, StandardCharsets.UTF_8);
    }

    public Mono<List<Boolean>> moveCustomerToEntryQueue(Integer backPressure) {
        return queueRepository.getRangedWaitingQueueItem(backPressure)
                .flatMap(queueItem -> queueRepository.joinQueueItem(queueItem.toEntryQueueItem())
                        .flatMap(newQueueItem -> {
                            log.info("Customer moved to entry queue: " + newQueueItem);
                            return queueRepository.deleteQueueItem(queueItem);
                        })
                )
                .collectList()
                .doOnNext(success -> log.info("Customers moved to entry queue: " + success))
                .onErrorResume(Mono::error);
    }
}