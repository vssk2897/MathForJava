package com.blackhat.lib;

import java.util.concurrent.TimeUnit;

public class StopWatch {
    long starts;

    public static StopWatch start() {
        return new StopWatch();
    }

    private StopWatch() {
        reset();
    }

    public StopWatch reset() {
        starts = System.nanoTime();
        return this;
    }

    public long time() {
        long ends = System.nanoTime();
        return ends - starts;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
}