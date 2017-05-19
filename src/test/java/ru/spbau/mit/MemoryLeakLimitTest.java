package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;

public class MemoryLeakLimitTest {
    private static int[] memory;
    private static final int TEST_MEMORY_SIZE = 100000;

    @Rule
    public final MemoryLeakLimit memoryLeakLimit = new MemoryLeakLimit();

    // CHECKSTYLE:OFF
    @Test
    public void testHugeMemoryAllocation() {
        memoryLeakLimit.limit(1);
        memory = new int[TEST_MEMORY_SIZE];
    }

    @Test
    public void testMemoryLeakAndException() throws Exception {
        memoryLeakLimit.limit(1);
        memory = new int[TEST_MEMORY_SIZE];
        throw new Exception("Some exception");
    }
    // CHECKSTYLE:ON

    @Test(expected = Exception.class)
    public void testException() throws Exception {
        throw new Exception("Some exception");
    }

    @Test
    public void testNoMemoryAllocation() {
        memoryLeakLimit.limit(0);
    }
}
