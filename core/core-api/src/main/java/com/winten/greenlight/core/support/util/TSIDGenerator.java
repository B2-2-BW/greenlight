package com.winten.greenlight.core.support.util;

import com.github.f4b6a3.tsid.TsidCreator;

public class TSIDGenerator {
    public static long tsid() {
        return TsidCreator.getTsid().toLong();
    }
}
