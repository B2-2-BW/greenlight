package com.winten.greenlight.core.domain;

import com.winten.greenlight.db.domain.QueueItem;
import com.winten.greenlight.db.domain.QueueType;

public record Customer(String eventId, String customerId, double entryTime) {

    public String toValue() {
        return String.format("%s:%s", eventId, customerId);
    }

    public Customer(String eventId, String customerId) {
        this(eventId, customerId, (double) System.nanoTime());
    }

    public QueueItem toWaitingQueueItem() {
        return new QueueItem(
                QueueType.WAIT,
                toValue(),
                entryTime
        );
    }
    public QueueItem toEntryQueueItem() {
        return new QueueItem(
                QueueType.ENTRY,
                toValue(),
                entryTime
        );
    }
}
