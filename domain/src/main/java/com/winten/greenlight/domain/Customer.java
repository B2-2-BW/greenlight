package com.winten.greenlight.domain;

public record Customer(String eventId, String customerId, double waitingScore, WaitingStatus waitingStatus,
                       AccessTicket accessTicket) {
}
