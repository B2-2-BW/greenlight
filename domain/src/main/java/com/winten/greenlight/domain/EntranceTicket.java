package com.winten.greenlight.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EntranceTicket(String ticketId, String eventId, String customerId, LocalDateTime issuedAt, String redirectUrl) {
}
