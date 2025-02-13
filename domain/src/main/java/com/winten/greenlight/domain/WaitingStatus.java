package com.winten.greenlight.domain;

public enum WaitingStatus {
    WAITING("queue:waiting"),
    READY("queue:ready");

    private final String queueName;

    WaitingStatus(String queueName) {
        this.queueName = queueName;
    }

    public String queueName() {
        return this.queueName;
    }
}
