package org.ignacioScript.co.util;

import java.util.concurrent.atomic.AtomicInteger;


public class IdGenerator {
    private static int counter = 0;

    public static int generateId() {
        return ++counter; // Increment and return the new value
    }

    public static void initializeCounter(int initialValue) {
        counter = initialValue; // Set the initial value
    }
}