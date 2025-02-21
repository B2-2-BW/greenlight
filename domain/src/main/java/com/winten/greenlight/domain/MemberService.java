package com.winten.greenlight.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 대기 정보 조회
    public Mono<MemberQueueResponse> getMemberQueueInfo(Customer customer) {
        return memberRepository.getMemberQueuePosition(customer)
            .zipWith(memberRepository.getTotalWaitingCount(customer))
            .map(tuple -> { //결과를 tuple로 묶어 하나의 Mono로 반환.
                int queueNumber = tuple.getT1(); // 현재 고객의 대기 순번
                int total = tuple.getT2(); // 전체 대기자 수
                int remaining;

                if (queueNumber == -1) {
                    queueNumber = 0; // 대기열에 없으면 0으로 보정
                    remaining = total; // 전체 대기자 수 그대로 사용
                } else {
                    remaining = total - queueNumber;
                }

                int estimatedTime = estimateWaitTime(remaining);
                return new MemberQueueResponse(queueNumber, remaining, estimatedTime);
            });
    }

    // 예상 대기시간 계산 (1명당 2분 예상)
    private int estimateWaitTime(int remainingMembers) {
        return remainingMembers * 2; // 1명당 2분 대기 예상
    }
}
