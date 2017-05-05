package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;

public class MemoryLeakLimitTest {

    private static int[] buf;

    @Rule
    public final MemoryLeakLimit memoryLeakLimit = new MemoryLeakLimit();


    @Test
    public void testFailed() {
        memoryLeakLimit.limit(1);
        buf = new int[2 * 1024 * 1024];
    }

}
