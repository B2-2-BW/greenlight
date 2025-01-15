package com.winten.greenlight.core.api.controller.v1;

import com.winten.greenlight.core.api.controller.v1.request.ExampleRequestDto;
import com.winten.greenlight.core.api.controller.v1.response.ExampleResponseDto;
import com.winten.greenlight.core.support.response.ApiResponse;
import com.winten.greenlight.domain.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {
    private final ExampleService exampleService;

    @PostMapping()
    public Mono<ApiResponse<ExampleResponseDto>> saveExample(@RequestBody ExampleRequestDto requestDto) {
        return exampleService.saveExample(requestDto.toExample())
                .map(example -> ApiResponse.success(new ExampleResponseDto(example)))
                .flatMap(response -> Mono.just(response));
    }
}
