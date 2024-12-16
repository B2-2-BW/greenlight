package com.winten.greenlight.core.domain.queue;

public record QueuedCustomer(String key, String value, double score, long rank) {
}
