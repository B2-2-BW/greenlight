package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Customer;

public class MemberZSetEntity implements BaseZSetEntity {

    Customer customer;

    public MemberZSetEntity(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String key() {
        return "";
    }

    @Override
    public String value() {
        return "";
    }

    @Override
    public double score() {
        return 0;
    }
}
