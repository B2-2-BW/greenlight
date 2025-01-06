package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.api.controller.v1.response.PollingResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.customer.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class QueueController {

    private final GuestService guestService;

    @PostMapping("{eventId}/guest/{guestId}/poll")
    public Mono<ApiResponse<PollingResponseDto>> getEventPolling(@PathVariable String eventId, @PathVariable String guestId) {
        return guestService.poll(eventId, guestId)
                .flatMap(result -> Mono.just(ApiResponse.success(PollingResponseDto.from(result))))
                .onErrorResume(e -> {
                    log.error("Failed to issue ticket: {}", e.getMessage());
                    return Mono.error(e);
                });
    }
}
