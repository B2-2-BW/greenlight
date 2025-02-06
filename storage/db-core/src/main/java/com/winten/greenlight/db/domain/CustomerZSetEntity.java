package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Customer;

public class CustomerZSetEntity implements BaseZSetEntity {
    Customer customer;

    public CustomerZSetEntity(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String key() {
        return "queue:customer";
    }

    @Override
    public String value() {
        return customer.eventId() + ":" + customer.customerId();
    }

    @Override
    public double score() {
        return customer.waitingScore();
    }
}
