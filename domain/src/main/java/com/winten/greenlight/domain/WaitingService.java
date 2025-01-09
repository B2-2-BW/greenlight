package com.winten.greenlight.domain;

import com.winten.greenlight.support.error.CoreException;
import com.winten.greenlight.support.error.ErrorType;
import com.winten.greenlight.support.util.IDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final CustomerRepository customerRepository;
    private final EventServiceWithCache eventServiceWithCache;

    public Mono<Waiting> register(String eventId, long waitingScore) {
        return waitingRepository.save(new Waiting(eventId, IDGenerator.generate(), waitingScore));
    }

    public Mono<WaitingPosition> findPosition(String eventId, String waitingId) {
        if (eventId == null) {
            return Mono.error(new CoreException(ErrorType.MISSING_EVENT_ID));
        }
        return eventServiceWithCache.findEventByEventId(eventId)
                .flatMap(__ -> waitingRepository.findRank(new Waiting(eventId, waitingId, 0L)))
                .zipWith(waitingRepository.size())
                .map(tuple -> new WaitingPosition(tuple.getT1(), tuple.getT2(), calculateEstimatedWaitingTime(tuple.getT1())))
                .switchIfEmpty(Mono.error(new CoreException(ErrorType.WAITING_NOT_FOUND)));
    }

    private long calculateEstimatedWaitingTime(long rank) {
        return rank; // TODO 예상 대기시간을 반환하는 비즈니스로직
    }

    public Mono<List<Boolean>> letWaitingComeIn(Integer count) {
        return waitingRepository.findAll(count)
                .flatMap(waiting -> customerRepository.save(waiting.toCustomer())
                                    .flatMap(__ -> waitingRepository.remove(waiting))
                                    .doOnNext(success -> log.info("Customer moved to ready queue: {}", success))
                )
                .collectList()
                .doOnNext(list -> log.info("Moved {} customers to ready queue", list.size()))
                .onErrorResume(Mono::error);
    }
}
