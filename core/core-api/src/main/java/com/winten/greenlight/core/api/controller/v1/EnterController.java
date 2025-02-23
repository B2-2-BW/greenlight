package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.api.controller.v1.request.EnterRequestDto;
import com.winten.greenlight.core.api.controller.v1.response.EnterResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.register.EnterService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/customer")
public class EnterController {
    private final EnterService enterService;

    public EnterController(EnterService enterService) {
        this.enterService = enterService;
    }

    @PostMapping("/enter")
    public Mono<ApiResponse<EnterResponseDto>> enterCustomer(@RequestBody EnterRequestDto requestDto) {
        EnterRequestDto updatedRequestDto = new EnterRequestDto(
            requestDto.eventId(),
            System.currentTimeMillis() // 새로운 대기 점수
        );

        return enterService.enterCustomer(updatedRequestDto.eventId(), updatedRequestDto.waitingScore())
            .map(customer -> ApiResponse.success(new EnterResponseDto("SUCCESS")));
    }
}
