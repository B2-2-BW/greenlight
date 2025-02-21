package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.domain.MemberQueueResponse;

public record MemberQueueResponseDto(int queueNumber, int remainingMembers, int estimatedWaitTime) {
    public MemberQueueResponseDto(MemberQueueResponse response) {
        this(response.queueNumber(), response.remainingMembers(), response.estimatedWaitTime());
    }
}
