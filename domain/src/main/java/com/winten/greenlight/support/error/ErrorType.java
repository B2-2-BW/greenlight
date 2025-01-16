package com.winten.greenlight.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.WARN),
    EXAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.E404, "Example not found.", LogLevel.INFO)
    ;

    private final HttpStatus status; //HTTP 응답 코드

    private final ErrorCode code; // 고유 오류 코드

    private final String message; // 노출 메시지

    private final LogLevel logLevel;

}
