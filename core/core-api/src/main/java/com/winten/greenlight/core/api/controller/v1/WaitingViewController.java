package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.domain.WaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/w")
@RequiredArgsConstructor
public class WaitingViewController {
    private final WaitingService waitingService;

    /* 이벤트 요청 */
    @GetMapping("/{eventId}")
    public Mono<String> getWaitingView(@PathVariable String eventId, Model model) {
        long waitingScore = System.currentTimeMillis();
        return Mono.just(waitingService.register(eventId, waitingScore))
                .doOnNext(waiting -> model.addAttribute("waiting", waiting))
                .flatMap(waiting -> Mono.just("waitingScreen"))
                .switchIfEmpty(Mono.just("notFound")
                        .doOnNext(__ -> log.info("Event not found: {}", eventId)));
    }

}
