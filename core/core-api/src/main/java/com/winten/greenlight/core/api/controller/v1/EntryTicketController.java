package com.winten.greenlight.core.api.controller.v1;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/entry-ticket")
public class EntryTicketController {
    @PostMapping("authenticate")
    public String authenticate() {
        return "{\"result\": \"success\"}";
    }
}
