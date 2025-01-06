package com.winten.greenlight.domain.customer;

public record Guest(String eventId, String guestId, WaitingStatus status, double entryTime) {

    public Guest(String eventId, String guestId, WaitingStatus status) {
        this(eventId, guestId, status, (double) System.nanoTime());
    }

    public static Guest waiting(String eventId, String guestId, double entryTime) {
        return new Guest(eventId, guestId, WaitingStatus.WAITING, entryTime);
    }
    public String tokenize() {
        return String.format("%s:%s:%s", eventId, guestId, entryTime);
    }

    public Guest updateStatus(WaitingStatus status) {
        return new Guest(eventId, guestId, status, entryTime);
    }
}
