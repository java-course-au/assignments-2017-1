package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;

public class MemoryLeakLimitTest {
    private static Integer[] memory;

    @Rule
    public final MemoryLeakLimit memoryLeakLimit = new MemoryLeakLimit();

    // CHECKSTYLE:OFF
    @Test
    public void testHugeMemoryAllocation() {
        memoryLeakLimit.limit(1);
        memory = new Integer[100000];
    }
    // CHECKSTYLE:ON

    @Test
    public void testNoMemoryAllocation() {
        memoryLeakLimit.limit(0);
    }
}
