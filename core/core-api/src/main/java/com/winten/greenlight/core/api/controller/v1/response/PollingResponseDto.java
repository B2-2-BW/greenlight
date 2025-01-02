package com.winten.greenlight.core.api.controller.v1.response;


import com.winten.greenlight.domain.customer.EntranceTicket;
import com.winten.greenlight.domain.customer.WaitingStatus;
import com.winten.greenlight.domain.customer.GuestStatus;

public record PollingResponseDto(WaitingStatus status, long position, long size, long estimatedWaitingTime, EntranceTicket entranceTicket) {
    public static PollingResponseDto from(GuestStatus guestStatus) {
        return new PollingResponseDto(guestStatus.status(), guestStatus.position(), guestStatus.size(), guestStatus.estimatedWaitingTime(), guestStatus.entranceTicket());
    }
}
