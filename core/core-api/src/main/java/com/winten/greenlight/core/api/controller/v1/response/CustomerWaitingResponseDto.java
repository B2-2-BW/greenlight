package com.winten.greenlight.core.api.controller.v1.response;

import com.winten.greenlight.domain.AccessTicket;

public record CustomerWaitingResponseDto(String eventId, String customerId, double waitingScore,
                                         AccessTicket accessTicket) {
}
