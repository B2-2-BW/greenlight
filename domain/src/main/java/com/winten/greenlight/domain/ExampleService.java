package com.winten.greenlight.domain;

import com.winten.greenlight.support.error.CoreException;
import com.winten.greenlight.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExampleService {
    private final ExampleRepository exampleRepository;

    public Mono<Example> saveExample(Example example) {
        return exampleRepository.save(example)
                .doOnNext(result -> log.info("saved!"))
                .flatMap(result -> {
                        if (result) {
                            return Mono.just(example);
                        }
                        return Mono.error(new CoreException(ErrorType.EXAMPLE_NOT_FOUND, "오류 발생"));
                });
    }
}
