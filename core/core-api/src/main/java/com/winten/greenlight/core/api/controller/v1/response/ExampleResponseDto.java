package com.winten.greenlight.core.api.controller.v1.response;

import com.winten.greenlight.domain.Example;

public record ExampleResponseDto(String v1, String v2) {
    public ExampleResponseDto(Example example) {
        this(example.v1(), example.v2());
    }
}
