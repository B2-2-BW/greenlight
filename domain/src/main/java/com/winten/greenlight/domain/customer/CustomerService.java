package com.winten.greenlight.domain.customer;

import com.winten.greenlight.support.error.CoreException;
import com.winten.greenlight.support.error.ErrorType;
import com.winten.greenlight.support.util.TSIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final TSIDGenerator tsidGenerator;
    private final WaitingGuestRepository waitingGuestRepository;

    public Mono<WaitingGuest> registerCustomerFor(String eventId) {
        String customerId = tsidGenerator.generate();
        WaitingGuest waitingGuest = new WaitingGuest(eventId, customerId, WaitingStatus.WAITING);
        return waitingGuestRepository.save(waitingGuest)
                .flatMap(Mono::just)
                .doOnError(e -> log.error("Register error: {}", e.getMessage()));
    }

    public Mono<WaitingTicket> poll(String eventId, String customerId) {
        WaitingGuest waitingGuest = new WaitingGuest(eventId, customerId, WaitingStatus.WAITING);
        return waitingGuestRepository.findRank(waitingGuest)
                .zipWith(waitingGuestRepository.size(WaitingStatus.WAITING.name()))
                .flatMap(tuple -> Mono.just(
                        WaitingTicket.builder()
                                .status(WaitingStatus.WAITING)
                                .position(tuple.getT1())
                                .size(tuple.getT2())
                                .estimatedWaitingTime(calculateEstimatedWaitingTime(tuple.getT1(), tuple.getT2()))
                                .ticketDetail(TicketDetail.empty())
                                .build()
                ))
                .switchIfEmpty(issueTicketIfReady(eventId, customerId))
                .doOnError(e -> log.error("Polling error: {}", e.getMessage()));
    }

    public Mono<WaitingTicket> issueTicketIfReady(String eventId, String customerId) {
        WaitingGuest waitingGuest = new WaitingGuest(eventId, customerId, WaitingStatus.READY);
        return isReady(waitingGuest)
                .doOnNext(ready -> log.info("Customer ready status: {}", ready))
                .flatMap(ready -> {
                    if (ready) {
                        return Mono.just(
                                WaitingTicket.builder()
                                        .status(WaitingStatus.READY)
                                        .position(0)
                                        .size(0)
                                        .estimatedWaitingTime(0)
                                        .ticketDetail(createTicket(waitingGuest))
                                        .build()
                        );
                    }
                    else {
                        return Mono.error(new CoreException(ErrorType.GUEST_NOT_FOUND));
                    }
                });
    }

    private Mono<Boolean> isReady(WaitingGuest waitingGuest) {
        return waitingGuestRepository.findRank(waitingGuest)
                .flatMap(rank -> Mono.just(true))
                .switchIfEmpty(Mono.just(false));
    }

    private long calculateEstimatedWaitingTime(long rank, long size) {
        return rank;
    }

    private TicketDetail createTicket(WaitingGuest waitingGuest) {
        String redirectUrl = "https://www.thehyundai.com/front/bda/BDALiveBrodViewer.thd?pLiveBfmtNo=202411130001";
        return TicketDetail.builder()
                .issuedAt(LocalDateTime.now())
                .owner(waitingGuest.customerId())
                .token(waitingGuest.tokenize())
                .redirectUrl(redirectUrl)
                .build();
    }

    public Mono<List<Boolean>> moveCustomerToEntryQueue(Integer backPressure) {
        return waitingGuestRepository.findAll(WaitingStatus.WAITING.name(), backPressure)
                .flatMap(waitingGuest -> {
                        var readyGuest = waitingGuest.convertTo(WaitingStatus.READY);
                        return waitingGuestRepository.save(readyGuest)
                                .flatMap(movedGuest -> waitingGuestRepository.remove(waitingGuest))
                                .doOnNext(success -> log.info("Customer moved to entry queue: " + success));
                        }
                )
                .collectList()
                .doOnNext(list -> log.info("Moved {} customers to entry queue", list.size()))
                .onErrorResume(Mono::error);
    }
}