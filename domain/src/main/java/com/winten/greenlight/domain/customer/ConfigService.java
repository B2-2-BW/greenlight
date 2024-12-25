package com.winten.greenlight.domain.customer;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ConfigService {
    @Cacheable("backPressure")
    public Mono<Integer> getBackPressure() {
        return Mono.just(1);
    }

    @Cacheable("entryUrl")
    public Mono<Map<String, String>> getEventUrlMap() {
        Map<String, String> urlCache = Map.of(
                "1", "https://www.thehyundai.com/front/bda/BDALiveBrodViewer.thd?pLiveBfmtNo=202411130001"
        );
        return Mono.just(urlCache);
    }
}
