package com.winten.greenlight.core.api.controller.v1.response;

import com.winten.greenlight.domain.WaitingPosition;

public record WaitingResponseDto(long position, long totalSize, long estimatedWaitingTime) {
    public WaitingResponseDto(WaitingPosition waitingPosition) {
        this(waitingPosition.position(), waitingPosition.totalSize(), waitingPosition.estimatedWaitingTime());
    }
}
