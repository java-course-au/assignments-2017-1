package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.junit.Assert.assertTrue;

public class MemoryLeakLimit implements TestRule {
    public static final int MB = 1024 * 1024;

    private long limit;

    @Override
    public Statement apply(final Statement statement,
                           final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final long occupiedMemoryBefore = getOccupiedMemory();
                statement.evaluate();
                final long occupiedMemoryAfter = getOccupiedMemory();
                assertTrue("Memory limit exceeded",
                        occupiedMemoryAfter - occupiedMemoryBefore <= limit);
            }

            private long getOccupiedMemory() {
                final Runtime runtime = Runtime.getRuntime();
                runtime.gc();
                return runtime.totalMemory() - runtime.freeMemory();
            }
        };
    }

    void limit(final long mb) {
        limit = mb * MB;
    }
}
