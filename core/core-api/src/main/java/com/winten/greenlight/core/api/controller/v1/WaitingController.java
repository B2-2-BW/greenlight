package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.api.controller.v1.response.WaitingResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.customer.WaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingController {
    private final WaitingService waitingService;

    /* 이벤트 요청 */
    @GetMapping("/{waitingId}")
    public Mono<ApiResponse<WaitingResponseDto>> getWaitingView(@RequestParam String eventId, @PathVariable String waitingId) {
        return waitingService.findPosition(eventId, waitingId)
                .flatMap(result -> Mono.just(ApiResponse.success(new WaitingResponseDto(result))))
                .doOnNext(result -> log.info("Event found: {}", eventId));
    }

}
