package com.winten.greenlight.core.api.controller;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.register.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/register/{eventId}")
    public Mono<Customer> register(@PathVariable String eventId) {
        return registerService.getTicket(eventId);
    }
}
