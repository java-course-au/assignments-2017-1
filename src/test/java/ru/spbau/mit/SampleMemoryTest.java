package ru.spbau.mit;

import org.junit.Rule;
import org.junit.Test;

public class SampleMemoryTest {
    @Rule
    public final MemoryLeakLimit memoryLimit = new MemoryLeakLimit();

    @Test
    public void testDoNothingNonzeroLimit() {
        memoryLimit.limit(4);
    }

    @Test
    public void testDoNothingZeroLimit() {
        memoryLimit.limit(0);
    }

    @Test
    public void testCreateGarbageViolateLimit() {
        memoryLimit.limit(1);
        MemoryConsumer consumer = new MemoryConsumer();
    }

    @Test
    public void testCreateGarbageNotViolateLimit() {
        memoryLimit.limit(1);
        MemoryConsumerSmall consumer = new MemoryConsumerSmall();
    }
}
