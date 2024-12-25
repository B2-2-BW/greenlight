package com.winten.greenlight.domain.customer;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TicketDetail(LocalDateTime issuedAt, String owner, String token, String redirectUrl) {
    public static TicketDetail empty() {
        return new TicketDetail(null, "", "", "");
    }
}
