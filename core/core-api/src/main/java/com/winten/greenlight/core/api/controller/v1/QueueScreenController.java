package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.domain.ConfigService;
import com.winten.greenlight.core.domain.QueueService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/e")
@RequiredArgsConstructor
public class QueueScreenController {
    private final QueueService queueService;
    private final ConfigService configService;

    @GetMapping("/{eventId}")
    public Mono<String> getQueueScreen(@PathVariable String eventId, Model model) {
        return configService.getEventUrlMap()
                        .flatMap(urlMap -> {
                            if (urlMap.containsKey(eventId)) {
                                return queueService.registerCustomerInWaitingQueue(eventId)
                                        .flatMap(customer -> {
                                            model.addAttribute("customer", customer);
                                            return Mono.just("queueScreen");
                                        });
                            }
                            else {
                                return Mono.just("notFound");
                            }
                        });
    }

    @GetMapping("/test")
    public Mono<String> getTestPage(@RequestParam String entryTicket, Model model) {
        model.addAttribute("entryTicket", URLDecoder.decode(entryTicket, StandardCharsets.UTF_8));
        return Mono.just("testPage");
    }
}
