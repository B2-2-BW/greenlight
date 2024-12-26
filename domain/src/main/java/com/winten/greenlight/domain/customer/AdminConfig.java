package com.winten.greenlight.domain.customer;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AdminConfig {
    @Cacheable("backPressure")
    public Mono<Integer> getBackPressure() {
        return Mono.just(1);
    }

    @Cacheable("eventUrlMap")
    public Mono<Map<String, String>> eventUrlMap() {
        Map<String, String> urlCache = Map.of(
                "1", "https://www.thehyundai.com/front/bda/BDALiveBrodViewer.thd?pLiveBfmtNo=202411130001"
        );
        return Mono.just(urlCache);
    }

    @Cacheable("eventUrl")
    public Mono<String> eventUrl(String eventId) {
        return eventUrlMap()
                .flatMap(map -> map.get(eventId) != null ? Mono.just(map.get(eventId)) : Mono.empty());
    }
}
