package com.winten.greenlight.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TicketGenerator {
    public EntranceTicket generate(String eventId, String customerId) {
        return EntranceTicket.builder()
                .ticketId("randomTicketId")
                .customerId(customerId)
                .eventId(eventId)
                .issuedAt(LocalDateTime.now())
                .redirectUrl("https://www.thehyundai.com/front/bda/BDALiveBrodViewer.thd?pLiveBfmtNo=202411130001")
                .build();
    }
}
