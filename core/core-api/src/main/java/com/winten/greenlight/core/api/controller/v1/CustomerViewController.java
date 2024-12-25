package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.domain.customer.ConfigService;
import com.winten.greenlight.domain.customer.CustomerService;
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
public class CustomerViewController {
    private final CustomerService customerService;
    private final ConfigService configService;

    /* 이벤트 요청 */
    @GetMapping("/{eventId}")
    public Mono<String> getWaitingView(@PathVariable String eventId, Model model) {
        return configService.getEventUrlMap()
                        .flatMap(urlMap -> {
                            if (urlMap.containsKey(eventId)) {
                                return customerService.registerCustomerFor(eventId)
                                        .doOnNext(guest -> model.addAttribute("guest", guest))
                                        .flatMap(guest -> Mono.just("queueScreen"));
                            }
                            else {
                                return Mono.just("notFound");
                            }
                        });
    }
}
