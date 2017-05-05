package ru.spbau.mit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MemoryLeakLimitTest {
    private static byte[] leakedMemory;

    @Rule
    public final MemoryLeakLimit memoryLeakLimit = new MemoryLeakLimit();

    @Before
    public void freeMemory() {
        leakedMemory = null;
    }

    @Test
    public void testZeroLimitPass() {
        memoryLeakLimit.limit(0);
    }

    @Test
    public void testZeroLimitFail() {
        memoryLeakLimit.limit(0);
        leakedMemory = new byte[MemoryLeakLimit.MB];
    }

    @Test
    public void testLimitPass() {
        memoryLeakLimit.limit(2);
        leakedMemory = new byte[MemoryLeakLimit.MB];
    }

    @Test
    public void testLimitFail() {
        memoryLeakLimit.limit(1);
        leakedMemory = new byte[MemoryLeakLimit.MB * 2];
    }
}
