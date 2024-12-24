package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.domain.PollingResult;
import com.winten.greenlight.core.domain.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/q")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/poll")
    public Mono<PollingResult> getPollingResult(@RequestBody PollingRequestDto requestDto) {
        return queueService.getPollingResultOfCustomer(requestDto.toCustomer())
                .onErrorResume(Mono::error);
    }
}
