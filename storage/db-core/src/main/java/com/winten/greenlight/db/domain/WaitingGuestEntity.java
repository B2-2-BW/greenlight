package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.customer.WaitingGuest;
import com.winten.greenlight.domain.customer.WaitingStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WaitingGuestEntity implements BaseZSetEntity {
    private String eventId;
    private String customerId;
    private WaitingStatus status;
    private double entryTime;

    @Override
    public String key() {
        return status.name();
    }

    @Override
    public String value() {
        return String.format("%s:%s", eventId, customerId);
    }

    @Override
    public double score() {
        return entryTime;
    }

    public static WaitingGuestEntity from(WaitingGuest waitingGuest) {
        return new WaitingGuestEntity(waitingGuest.eventId(), waitingGuest.customerId(), waitingGuest.status(), waitingGuest.entryTime());
    }

    public WaitingGuest toWaitingGuest() {
        return new WaitingGuest(eventId, customerId, status, entryTime);
    }


    public static WaitingGuestEntity parse(String key, String value, double score) {
        var parts = value.split(":");
        String eventId = parts[0];
        String customerId = parts[1];
        WaitingStatus status = WaitingStatus.valueOf(key);
        return new WaitingGuestEntity(eventId, customerId, status, score);
    }
}
