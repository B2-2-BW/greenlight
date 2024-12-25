package com.winten.greenlight.domain.customer;

import lombok.Builder;

@Builder
public record WaitingTicket(WaitingStatus status, long position, long size, long estimatedWaitingTime, TicketDetail ticketDetail) {
}
