package ru.spbau.mit;


import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class MemoryLeakLimitTest {
    private static int[] memory;
    private static final int TEST_MEMORY_SIZE = 1_000_000;
    private static final Random RNG = new Random();

    @Rule
    public final MemoryLeakLimit memoryLeakLimit = new MemoryLeakLimit();

    @After
    public void after() {
        if (memory != null) {
            memory[RNG.nextInt(TEST_MEMORY_SIZE)] =
                    memory[RNG.nextInt(TEST_MEMORY_SIZE)];
        }
    }

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

    @Test(expected = Exception.class)
    public void testException() throws Exception {
        memoryLeakLimit.limit(0);
        throw new Exception("Some exception");
    }

    @Test
    public void testNoMemoryAllocation() {
        memoryLeakLimit.limit(1);
    }
}
