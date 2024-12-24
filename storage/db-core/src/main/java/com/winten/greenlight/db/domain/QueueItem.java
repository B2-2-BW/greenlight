package com.winten.greenlight.db.domain;

public record QueueItem(QueueType queueType, String member, Double entryTime) {
    public QueueItem toEntryQueueItem() {
        return new QueueItem(QueueType.ENTRY, member, entryTime);
    }
}
