package com.winten.greenlight.domain.customer;

import org.springframework.stereotype.Service;

@Service
public class QueueManager {
    public void addToWaitingQueue(Guest guest) {}

    public void moveToReadyQueue(Guest guest) {}
    public void removeFromWaitingQueue(Guest guest) {}

}
