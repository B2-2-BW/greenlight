package com.winten.greenlight.core.api.controller.v1.response;


import com.winten.greenlight.domain.customer.TicketDetail;
import com.winten.greenlight.domain.customer.WaitingStatus;
import com.winten.greenlight.domain.customer.WaitingTicket;

public record WaitingResponseDto(WaitingStatus status, long position, long size, long estimatedWaitingTime, TicketDetail ticketDetail) {
    public static WaitingResponseDto from(WaitingTicket waitingTicket) {
        return new WaitingResponseDto(waitingTicket.status(), waitingTicket.position(), waitingTicket.size(), waitingTicket.estimatedWaitingTime(), waitingTicket.ticketDetail());
    }
}
