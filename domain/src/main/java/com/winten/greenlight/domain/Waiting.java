package com.winten.greenlight.domain;

public record Waiting(String eventId, String waitingId, double waitingScore) {
    public Customer toCustomer() {
        return new Customer(eventId, waitingId, waitingScore);
    }
}
