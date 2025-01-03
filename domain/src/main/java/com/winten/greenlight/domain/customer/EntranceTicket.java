package com.winten.greenlight.domain.customer;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EntranceTicket(String ticketId, LocalDateTime issuedAt, String redirectUrl) {
    public static EntranceTicket empty() {
        return new EntranceTicket("", null, "");
    }
}
