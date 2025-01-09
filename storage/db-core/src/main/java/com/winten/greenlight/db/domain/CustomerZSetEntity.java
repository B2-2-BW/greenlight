package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Customer;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

@AllArgsConstructor
public class CustomerZSetEntity implements BaseZSetEntity {
    public static final String KEY = "queue:customer";
    private String eventId;
    private String customerId;
    private Double waitingScore;

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public String value() {
        return String.format("%s:%s", eventId, customerId);
    }

    @Override
    public double score() {
        return waitingScore;
    }

    public static CustomerZSetEntity from(Customer customer) {
        return new CustomerZSetEntity(customer.eventId(), customer.customerId(), customer.waitingScore());
    }
    public static CustomerZSetEntity zSet(String key, ZSetOperations.TypedTuple<String> tuple) {
        assert key != null : "key is null";
        assert tuple != null && tuple.getValue() != null : "value is null";

        var parts = tuple.getValue().split(":");
        var eventId = parts[0];
        var customerId = parts[1];
        return new CustomerZSetEntity(eventId, customerId, tuple.getScore());
    }
    public Customer toWaiting() {
        return new Customer(eventId, customerId, waitingScore);
    }
}
