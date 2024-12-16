package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.domain.queue.ReactiveQueueManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/event")
public class QueueScreenController {
    private final ReactiveQueueManager queueManager;

    public QueueScreenController(ReactiveQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @GetMapping("/queue")
    public Mono<String> getQueueScreen(Model model) {
        return queueManager.getQueuedCustomer()
                        .flatMap(queuedCustomer -> {
                            model.addAttribute("customer", queuedCustomer);
                            return Mono.just("queueScreen");
                        });
    }
}
