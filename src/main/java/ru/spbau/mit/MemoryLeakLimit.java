package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {
    private static final long MB = 1024 * 1024;
    private long memoryLimit;

    public void limit(final long limit) {
        memoryLimit = limit * MB;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long freeMemoryBefore = getOccupiedMemory();
                statement.evaluate();
                long freeMemoryAfter = getOccupiedMemory();

                if (freeMemoryBefore - freeMemoryAfter > memoryLimit) {
                    throw new Exception("memory limit exceeded");
                }
            }

            private long getOccupiedMemory() {
                System.gc();
                return Runtime.getRuntime().freeMemory();
            }
        };
    }
}
