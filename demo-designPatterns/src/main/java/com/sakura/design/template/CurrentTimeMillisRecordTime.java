package com.sakura.design.template;

import java.util.function.Consumer;

public class CurrentTimeMillisRecordTime extends AbstractRecordTimeTemplate {

    @Override
    public long timing(Consumer cu) {
        long start = System.currentTimeMillis();

        cu.accept("");

        long end = System.currentTimeMillis();
        return end - start;
    }
}
