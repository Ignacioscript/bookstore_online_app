package org.ignacioScript.co.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    public static final AtomicInteger counter = new AtomicInteger();

    public static int generateId() {
        return counter.incrementAndGet();
    }
}
