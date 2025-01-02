package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.domain.customer.AdminConfig;
import com.winten.greenlight.domain.customer.GuestService;
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
public class GuestViewController {
    private final GuestService guestService;
    private final AdminConfig adminConfig;

    /* 이벤트 요청 */
    @GetMapping("/{eventId}")
    public Mono<String> getWaitingView(@PathVariable String eventId, Model model) {
        return adminConfig.eventUrl(eventId)
                .flatMap(url -> guestService.register(eventId)
                                                .doOnNext(guest -> model.addAttribute("guest", guest))
                                                .flatMap(guest -> Mono.just("queueScreen")))
                .switchIfEmpty(Mono.just("notFound").doOnNext(__ -> log.info("Event not found: {}", eventId)));
    }
}
