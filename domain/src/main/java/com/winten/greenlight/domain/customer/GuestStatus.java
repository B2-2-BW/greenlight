package com.winten.greenlight.domain.customer;

import lombok.Builder;

@Builder
public record GuestStatus(WaitingStatus status, long position, long size, long estimatedWaitingTime, EntranceTicket entranceTicket) {
}
