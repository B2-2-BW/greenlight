package com.winten.greenlight.db.domain.register;

import com.winten.greenlight.db.domain.BaseZSetEntity;
import com.winten.greenlight.domain.Customer;

public class TempZSetEntity implements BaseZSetEntity {
    private final String key;
    private final String value;
    private final double score;

    public TempZSetEntity(Customer customer) {
        this.key = customer.eventId();
        this.value = customer.customerId();
        this.score = customer.waitingScore();
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public double score() {
        return this.score;
    }
}
