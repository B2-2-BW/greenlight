package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.domain.customer.AdminConfig;
import com.winten.greenlight.domain.customer.GuestService;
import com.winten.greenlight.domain.customer.QueueService;
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
@RequestMapping("/e")
@RequiredArgsConstructor
public class GuestController {
    private final GuestService guestService;

    /* 이벤트 요청 */
    @GetMapping("/{eventId}")
    public Mono<String> getWaitingView(@PathVariable String eventId, Model model) {
        long entryTime = System.nanoTime();
        return Mono.just(guestService.register(eventId, entryTime))
                .doOnNext(guest -> model.addAttribute("guest", guest))
                .flatMap(guest -> Mono.just("waitingScreen"))
                .switchIfEmpty(Mono.just("notFound")
                        .doOnNext(__ -> log.info("Event not found: {}", eventId)));
    }
}
