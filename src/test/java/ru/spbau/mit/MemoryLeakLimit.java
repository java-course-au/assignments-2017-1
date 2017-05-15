package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {
    private long limit = 0;

    public void limit(long mb) {
        this.limit = mb * 1024 * 1024;
    }

    @Override
    public Statement apply(final Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Runtime runtime = Runtime.getRuntime();
                runtime.gc();
                long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
                statement.evaluate();
                runtime.gc();
                long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                if (usedMemoryAfter - usedMemoryBefore > limit) {
                    throw new AssertionError("MemoryLeakLimit");
                }
            }
        };
    }
}
