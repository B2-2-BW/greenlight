package com.winten.greenlight.domain.customer;

import com.winten.greenlight.support.util.TSIDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final TSIDGenerator tsidGenerator;
    private final QueueRepository queueRepository;

    public Mono<Guest> addToWaitingQueue(Guest guest) {
        return queueRepository.save(guest);
    }

    public void moveToReadyQueue(Guest guest) {}
    public void removeFromWaitingQueue(Guest guest) {}

}
