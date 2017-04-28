package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {
    private static final long bytesInMb = 1024 * 1024;
    private long mbLimit;

    public void limit(long mb) {
        mbLimit = mb;
    }

    @Override
    public Statement apply(final Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Runtime runtime = Runtime.getRuntime();
                runtime.gc();
                long occupiedMemBefore = runtime.totalMemory() - runtime.freeMemory();

                statement.evaluate();

                runtime.gc();
                long occupiedMemAfter = runtime.totalMemory() - runtime.freeMemory();
                long memoryIncreaseBytes = occupiedMemAfter - occupiedMemBefore;

                if (memoryIncreaseBytes > mbLimit * bytesInMb) {
                    throw new Exception("Memory limit violated");
                }
            }
        };
    }
}
