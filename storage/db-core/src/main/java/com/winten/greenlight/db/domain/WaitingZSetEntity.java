package com.winten.greenlight.db.domain;

import com.winten.greenlight.domain.customer.Waiting;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

@AllArgsConstructor
public class WaitingZSetEntity implements BaseZSetEntity {
    public static final String KEY = "queue:wait";
    private String eventId;
    private String waitingId;
    private Double waitingScore;

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public String value() {
        return String.format("%s:%s", eventId, waitingId);
    }

    @Override
    public double score() {
        return waitingScore;
    }

    public static WaitingZSetEntity from(Waiting waiting) {
        return new WaitingZSetEntity(waiting.eventId(), waiting.waitingId(), waiting.waitingScore());
    }
    public static WaitingZSetEntity zSet(String key, ZSetOperations.TypedTuple<String> tuple) {
        assert key != null : "key is null";
        assert tuple != null && tuple.getValue() != null : "value is null";

        var parts = tuple.getValue().split(":");
        var eventId = parts[0];
        var waitingId = parts[1];
        return new WaitingZSetEntity(eventId, waitingId, tuple.getScore());
    }
    public Waiting toWaiting() {
        return new Waiting(eventId, waitingId, waitingScore);
    }
}
