package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.domain.Customer;
import com.winten.greenlight.domain.ReadyService;
import com.winten.greenlight.domain.WaitingStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class ReadyController {
    private final ReadyService readyService;

    public ReadyController(ReadyService readyService) {
        this.readyService = readyService;
    }

    /**
     * 0. endpoint에 대한 고민 : /readyQueue/events/{eventId}/customers/{customerId}
     * 1. 현재 변동된 CustomerZsetEntity의 table name(key)의 return 구조상
     * 현재 API의 endpoint 경로로 불러오는 것이 아닌 해당고객의 상태조회로 통합되어야 할 방향성이 더 높음
     * (새롭게 바뀐 CustomerZsetEntity는 현재 고객의 상태 status에 따라서
     * ,조회할 테이블 key가 return되고 해당 테이블을 조회하도록 되어있기 때문에)
     * 2. 즉, redis 테이블 내에서 해당 고객의 status 값데이터도 가지고 있는가?
     * 가 테이블 내에 고객이 존재하는지 검증하는 로직에서 필수 요건이 될 수도 있음
     * (로직을 어떻게 구성하느냐에 따라 다를듯, 논의 필요)
     *
     * @param eventId
     * @param customerId
     * @return
     */
    @GetMapping("/readyQueue/events/{eventId}/customers/{customerId}")
    public Mono<Boolean> isReady(@PathVariable String eventId, @PathVariable String customerId) { //접속 여부 확인
        // 현재는 api의 통합 이전 단계이기 때문에 임의로 new Customer record 생성자 호출시에
        // 강제로 WatingStatus.READY 로 하드코딩함(무조건 queue:ready 테이블을 조회하기 위해)
        return readyService.isReady(new Customer(eventId, customerId, -1, WaitingStatus.READY))
            .doOnNext(result -> log.info("################isReady({}, {}, {})", eventId, customerId, result));
    }

}
