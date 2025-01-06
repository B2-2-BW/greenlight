package com.winten.greenlight.domain.customer;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AdminConfig {
    @Cacheable("backPressure")
    public Integer getBackPressure() {
        return 1;
    }

    @Cacheable("eventUrl")
    public String eventUrl(String eventId) {
        return "https://www.thehyundai.com/front/bda/BDALiveBrodViewer.thd?pLiveBfmtNo=202411130001";
    }
}
