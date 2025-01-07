package com.winten.greenlight.support.util;

import com.github.f4b6a3.tsid.TsidCreator;

public class IDGenerator {
    public static String generate() {
        return TsidCreator.getTsid().toString();
    }
}
