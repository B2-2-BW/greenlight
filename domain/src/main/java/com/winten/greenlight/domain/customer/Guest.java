package com.winten.greenlight.domain.customer;

public record Guest(String eventId, String guestId, WaitingStatus status, double entryTime) {

    public Guest(String eventId, String guestId, WaitingStatus status) {
        this(eventId, guestId, status, (double) System.nanoTime());
    }

    public String tokenize() {
        return String.format("%s:%s:%s", eventId, guestId, entryTime);
    }

    public Guest updateStatus(WaitingStatus status) {
        return new Guest(eventId, guestId, status, entryTime);
    }
}
