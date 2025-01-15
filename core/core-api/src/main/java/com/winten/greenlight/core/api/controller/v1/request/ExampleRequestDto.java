package com.winten.greenlight.core.api.controller.v1.request;

import com.winten.greenlight.domain.Example;

public record ExampleRequestDto(String v1, String v2, String v3, String v4) {
    public Example toExample() {
        return new Example(v1, v2);
    }
}
