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
                Throwable error = null;

                try {
                    long freeMemoryBefore = getOccupiedMemory();
                    try {
                        statement.evaluate();
                    } catch (Throwable e) {
                        error = e;
                    }
                    long freeMemoryAfter = getOccupiedMemory();

                    if (freeMemoryBefore - freeMemoryAfter > memoryLimit) {
                        throw new Exception("memory limit exceeded");
                    }
                } catch (Exception e) {
                    throw new Exception("memory check failed");
                }

                if (error != null) {
                    throw error;
                }
            }

            private long getOccupiedMemory() {
                System.gc();
                return Runtime.getRuntime().freeMemory();
            }
        };
    }
}
