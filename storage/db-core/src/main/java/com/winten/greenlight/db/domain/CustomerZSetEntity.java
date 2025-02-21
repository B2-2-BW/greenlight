package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.WaitingStatus;
import lombok.Setter;
import lombok.ToString;

@ToString
public class CustomerZSetEntity implements BaseZSetEntity {
    private final String eventId;
    private final String customerId;
    private final double waitingScore;
    @Setter
    private WaitingStatus waitingStatus;

    public CustomerZSetEntity(Customer customer) {
        this.eventId = customer.eventId();
        this.customerId = customer.customerId();
        this.waitingScore = customer.waitingScore();
        this.waitingStatus = customer.waitingStatus();
    }

    @Override
    public String key() {
        return waitingStatus.queueName();
    }

    @Override
    public String value() {
        return eventId + ":" + customerId;
    }

    @Override
    public double score() {
        return waitingScore;
    }
}
