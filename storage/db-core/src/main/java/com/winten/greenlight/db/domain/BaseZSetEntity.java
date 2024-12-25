package com.winten.greenlight.db.domain;

public interface BaseZSetEntity {
    String key();
    String value();
    double score();
}
