package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {

    private static final long MB_TO_BYTES = 1024 * 1024;
    private long limit;

    @Override
    public Statement apply(final Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Runtime runtime = Runtime.getRuntime();
                runtime.gc();
                long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
                Throwable throwable = null;
                try {
                    statement.evaluate();
                } catch (Throwable e) {
                    throwable = e;
                }
                runtime.gc();
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();

                if (memoryAfter - memoryBefore > limit) {
                    throw new Exception("memory leak");
                }

                if (throwable != null) {
                    throw throwable;
                }
            }
        };
    }

    public void limit(long mb) {
        limit = mb * MB_TO_BYTES;
    }
}
