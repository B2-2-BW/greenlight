package com.winten.greenlight.domain.customer;

public record WaitingGuest(String eventId, String customerId, WaitingStatus status, double entryTime) {

    public WaitingGuest(String eventId, String customerId, WaitingStatus status) {
        this(eventId, customerId, status, (double) System.nanoTime());
    }

    public String tokenize() {
        return String.format("%s:%s:%s", eventId, customerId, entryTime);
    }

    public WaitingGuest convertTo(WaitingStatus status) {
        return new WaitingGuest(eventId, customerId, status, entryTime);
    }
}
