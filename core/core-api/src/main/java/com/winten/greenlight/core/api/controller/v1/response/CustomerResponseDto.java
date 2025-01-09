package com.winten.greenlight.core.api.controller.v1.response;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.EntranceTicket;

public record CustomerResponseDto(String eventId, String customerId, double waitingScore, EntranceTicket ticket) {
    public CustomerResponseDto(Customer customer) {
        this(customer.eventId(), customer.customerId(), customer.waitingScore(), customer.ticket());
    }
}
