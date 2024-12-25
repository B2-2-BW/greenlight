package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.api.controller.v1.request.WaitingRequestDto;
import com.winten.greenlight.core.api.controller.v1.response.WaitingResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController()
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingController {

    private final CustomerService customerService;

    @PostMapping("/poll")
    public Mono<ApiResponse<WaitingResponseDto>> getWaiting(@RequestBody WaitingRequestDto requestDto) {
        return customerService.poll(requestDto.eventId(), requestDto.customerId())
                .flatMap(result -> Mono.just(ApiResponse.success(WaitingResponseDto.from(result))))
                .onErrorResume(e -> {
                    log.error("Failed to issue ticket: " + e.getMessage());
                    return Mono.error(e);
                });
    }
}
