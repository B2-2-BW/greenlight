package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.domain.Customer;

public record PollingRequestDto(String eventId, String customerId) {
    public Customer toCustomer() {
        return new Customer(eventId, customerId);
    }
}
