package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.customer.Guest;
import com.winten.greenlight.domain.customer.WaitingStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GuestEntity implements BaseZSetEntity {
    private String eventId;
    private String guestId;
    private WaitingStatus status;
    private double entryTime;

    @Override
    public String key() {
        return status.name();
    }

    @Override
    public String value() {
        return String.format("%s:%s", eventId, guestId);
    }

    @Override
    public double score() {
        return entryTime;
    }

    public static GuestEntity from(Guest guest) {
        return new GuestEntity(guest.eventId(), guest.guestId(), guest.status(), guest.entryTime());
    }

    public Guest toGuest() {
        return new Guest(eventId, guestId, status, entryTime);
    }


    public static GuestEntity parse(String key, String value, double score) {
        var parts = value.split(":");
        var eventId = parts[0];
        var guestId = parts[1];
        var status = WaitingStatus.valueOf(key);
        return new GuestEntity(eventId, guestId, status, score);
    }
}
