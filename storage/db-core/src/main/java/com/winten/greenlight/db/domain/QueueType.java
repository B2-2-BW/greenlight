package com.winten.greenlight.db.domain;

public enum QueueType {
    WAIT,
    ENTRY,
    EXIT
    ;
    public static QueueType of(String key) {
        return QueueType.valueOf(key.toUpperCase());
    }
}
