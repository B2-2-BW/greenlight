package com.winten.greenlight.core.api.controller;

import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.support.error.CoreException;
import com.winten.greenlight.support.error.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CoreException.class)
    public Mono<ResponseEntity<ApiResponse<?>>> handleCoreException(CoreException ex) {
        switch (ex.getErrorType().getLogLevel()) {
            case ERROR -> log.error("CoreException : {}", ex.getMessage(), ex);
            case WARN -> log.warn("CoreException : {}", ex.getMessage(), ex);
            default -> log.info("CoreException : {}", ex.getMessage(), ex);
        }
        return Mono.just(
            new ResponseEntity<>(ApiResponse.error(ex.getErrorType(), ex.getData()), ex.getErrorType().getStatus())
        );
    }

    //    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        log.error("Exception : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.error(ErrorType.DEFAULT_ERROR), ErrorType.DEFAULT_ERROR.getStatus());
    }

    // TODO Map이 아닌 ApiResponse를 리턴하도록 개선하고, 에러메시지 표출방식 통일
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleServerWebInputException(
        ServerWebInputException ex) {
        String parameterName = ex.getMethodParameter().getParameterName();
        String parameterType = ex.getMethodParameter().getParameterType().getSimpleName();

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Missing required request parameter");
        errorResponse.put("parameterName", parameterName);
        errorResponse.put("parameterType", parameterType);

        return Mono.just(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse));
    }

}

