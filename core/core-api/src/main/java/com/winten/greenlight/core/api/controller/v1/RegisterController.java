package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.api.controller.v1.request.RegisterRequestDto;
import com.winten.greenlight.core.api.controller.v1.response.RegisterResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.register.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/customer")
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public Mono<ApiResponse<RegisterResponseDto>> register(@RequestBody RegisterRequestDto requestDto) {
        return registerService.generateCustomer(requestDto.eventId())
            .map(customer -> ApiResponse.success(new RegisterResponseDto("SUCCESS")));
    }
}
