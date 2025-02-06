package com.winten.greenlight.core.api.controller;

import com.winten.greenlight.core.api.controller.v1.response.CustomerWaitingResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.CustomerWaitingService;
import com.winten.greenlight.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerWaitingController {
    private final CustomerWaitingService customerWaitingService;

    //고객 조회
    @PostMapping("/")
    public Mono<ApiResponse<CustomerWaitingResponseDto>> selectCustomer(@RequestBody Customer customer) {
        return customerWaitingService.getCustomer(customer.eventId(), customer.customerId())
            .flatMap(customerVO -> {
                CustomerWaitingResponseDto customerWaitingResponseDto = new CustomerWaitingResponseDto(customerVO.eventId(), customerVO.customerId(), customerVO.waitingScore(), customerVO.accessTicket());
                return Mono.just(ApiResponse.success(customerWaitingResponseDto));
            });
    }

    //고객 이탈(삭제)
    @PostMapping("/delete")
    public Mono<ApiResponse> deleteCustomer(@RequestBody Customer customer) {
        return customerWaitingService.deleteCustomer(customer.eventId(), customer.customerId())
            .flatMap(responseFlag -> {
                if (responseFlag) { // 삭제 성공
                    return Mono.just(ApiResponse.success(customer));
                } else { // 삭제 실패 ( 데이터 없음
                    return Mono.just(ApiResponse.error(ErrorType.EXAMPLE_NOT_FOUND, "조회되는 고객이 없습니다"));
                }
            });
    }
}
