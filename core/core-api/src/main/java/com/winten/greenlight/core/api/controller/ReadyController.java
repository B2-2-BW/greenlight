package com.winten.greenlight.core.api.controller;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.ReadyService;
import com.winten.greenlight.domain.WaitingStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class ReadyController {
    private final ReadyService readyService;

    public ReadyController(ReadyService readyService) {
        this.readyService = readyService;
    }

    @GetMapping("/isReady")
    public Mono<Boolean> isReady(@BindParam String eventId, @BindParam String customerId) { //접속 여부 확인
        System.out.println("isReady");
        System.out.println("eventId: " + eventId);
        System.out.println("customerId: " + customerId);

        var isReady = readyService.isReady(new Customer(eventId, customerId, -1, WaitingStatus.READY));
        System.out.println("isReady: " + isReady);

        return readyService.isReady(new Customer(eventId, customerId, -1, null));
    }

}
