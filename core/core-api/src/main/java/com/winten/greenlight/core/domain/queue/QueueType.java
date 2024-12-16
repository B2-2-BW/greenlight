package com.winten.greenlight.core.domain.queue;

public enum QueueType {
    WAIT("queue:wait"),
    READY("queue:ready"),
    EXIT("queue:exit")
    ;

    private final String queueName;

    QueueType(String queueName) {
        this.queueName = queueName;
    }
    public String queueName() { return queueName; }
}
