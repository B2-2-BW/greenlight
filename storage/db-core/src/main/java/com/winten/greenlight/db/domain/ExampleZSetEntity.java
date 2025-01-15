package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.Example;

public class ExampleZSetEntity implements BaseZSetEntity {
    public ExampleZSetEntity(Example example) {
    }

    @Override
    public String key() {
        return "";
    }

    @Override
    public String value() {
        return "";
    }

    @Override
    public double score() {
        return 0;
    }
}
