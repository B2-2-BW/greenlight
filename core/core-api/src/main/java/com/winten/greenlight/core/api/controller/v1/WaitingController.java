package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.api.controller.v1.request.WaitingRequestDto;
import com.winten.greenlight.core.api.controller.v1.response.WaitingResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.WaitingService;
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

    @PostMapping("/poll")
    public Mono<ApiResponse<WaitingResponseDto>> getWaitingView(@RequestBody WaitingRequestDto waitingRequestDto) {
        return waitingService.findPosition(waitingRequestDto.eventId(), waitingRequestDto.waitingId())
                .flatMap(result -> Mono.just(ApiResponse.success(new WaitingResponseDto(result))))
                .doOnNext(result -> log.info("Event found: {}", waitingRequestDto.eventId()));
    }

}
