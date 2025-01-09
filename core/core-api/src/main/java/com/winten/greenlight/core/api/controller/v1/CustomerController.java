package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.api.controller.v1.response.CustomerResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("{customerId}")
    public Mono<ApiResponse<CustomerResponseDto>> getEventPolling(@RequestParam String eventId, @PathVariable String customerId) {
        return customerService.find(eventId, customerId)
                .flatMap(result -> Mono.just(ApiResponse.success(new CustomerResponseDto(result))))
                .onErrorResume(e -> {
                    log.error("Failed to issue ticket: {}", e.getMessage());
                    return Mono.error(e);
                });
    }
}
