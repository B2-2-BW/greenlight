package com.winten.greenlight.domain;

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
}
