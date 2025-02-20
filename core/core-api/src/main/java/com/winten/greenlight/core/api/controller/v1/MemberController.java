package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.MemberService;
import com.winten.greenlight.domain.WaitingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberId}/queue")
    public Mono<ApiResponse<MemberQueueResponseDto>> getMemberQueueInfo(@PathVariable String memberId) {
        //eventId: parameter로 전달 받자.

        return memberService.getMemberQueueInfo(new Customer("1234", memberId, -1, WaitingStatus.READY))
            .map(MemberQueueResponseDto::new) // MemberQueueResponseDto로 변환
            .map(ApiResponse::success);
    }
}
