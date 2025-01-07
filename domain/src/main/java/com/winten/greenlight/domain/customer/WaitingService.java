package com.winten.greenlight.domain.customer;

import com.winten.greenlight.support.error.CoreException;
import com.winten.greenlight.support.error.ErrorType;
import com.winten.greenlight.support.util.IDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;

    public Mono<Waiting> register(String eventId, long waitingScore) {
        return waitingRepository.save(new Waiting(eventId, IDGenerator.generate(), waitingScore));
    }

    public Mono<WaitingPosition> findPosition(String eventId, String waitingId) {
        return waitingRepository.findRank(new Waiting(eventId, waitingId, 0L))
                .zipWith(waitingRepository.size())
                .map(tuple -> new WaitingPosition(tuple.getT1(), tuple.getT2(), calculateEstimatedWaitingTime(tuple.getT1())))
                .switchIfEmpty(Mono.error(new CoreException(ErrorType.WAITING_NOT_FOUND)));
    }

    private long calculateEstimatedWaitingTime(long rank) {
        return rank; // TODO 예상 대기시간을 반환하는 비즈니스로직
    }


//    public Mono<List<Boolean>> letWaitingComeIn(Integer count) {
//        return waitingRepository.findAll(count)
//                .flatMap(waiting -> {
//                            var readyGuest = waitingGuest.updateStatus(WaitingStatus.READY);
//                            return queueRepository.save(readyGuest)
//                                    .flatMap(__ -> queueRepository.remove(waitingGuest))
//                                    .doOnNext(success -> log.info("Customer moved to ready queue: {}", success));
//                        }
//                )
//                .collectList()
//                .doOnNext(list -> log.info("Moved {} customers to ready queue", list.size()))
//                .onErrorResume(Mono::error);
//    }
}
