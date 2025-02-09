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
    public Mono<MemberQueueResponse> getMemberQueueInfo(String memberId) {
        // 대기순번과 대기 인원 수를 조회
        return memberRepository.getMemberQueuePosition(memberId)
            .flatMap(position -> memberRepository.getTotalWaitingCount()
                .map(total -> new MemberQueueResponse(position, total - position, estimateWaitTime(total - position)))
            );
    }

    // 예상 대기시간 계산 (1명당 2분 예상)
    private int estimateWaitTime(int remainingMembers) {
        return remainingMembers * 2; // 1명당 2분 대기 예상
    }
}




