package com.winten.greenlight.domain.customer;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EntranceTicket(LocalDateTime issuedAt, String owner, String token, String redirectUrl) {
    public static EntranceTicket empty() {
        return new EntranceTicket(null, "", "", "");
    }
}
