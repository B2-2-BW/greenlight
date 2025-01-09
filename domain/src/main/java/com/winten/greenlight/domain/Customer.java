package com.winten.greenlight.domain;

public record Customer(String eventId, String customerId, double waitingScore, EntranceTicket ticket) {
    public Customer(String eventId, String customerId, double waitingScore) {
        this(eventId, customerId, waitingScore, null);
    }
}
