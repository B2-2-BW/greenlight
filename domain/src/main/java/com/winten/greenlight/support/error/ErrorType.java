package com.winten.greenlight.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.WARN),

    GUEST_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.E404, "Illegal access. Guest was not found in any level.", LogLevel.WARN),
    ;

    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;

}
