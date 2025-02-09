package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/queue")
    public Mono<ApiResponse<MemberQueueResponseDto>> getMemberQueueInfo(@RequestParam String memberId) {
        return memberService.getMemberQueueInfo(memberId)
            .map(MemberQueueResponseDto::new) // MemberQueueResponseDto로 변환
            .map(ApiResponse::success);
    }
    //http://localhost:8080/member/queue?memberId=0JENEC2Y643A7
}








