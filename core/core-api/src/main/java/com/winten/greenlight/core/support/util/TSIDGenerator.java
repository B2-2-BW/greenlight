package com.winten.greenlight.core.support.util;

import com.github.f4b6a3.tsid.TsidCreator;
import org.springframework.stereotype.Component;

@Component
public class TSIDGenerator {
    public String generate() {
        return TsidCreator.getTsid().toString();
    }
}
