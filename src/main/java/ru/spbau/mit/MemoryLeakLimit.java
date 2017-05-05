package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {

    private long limit;

    @Override
    public Statement apply(final Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Runtime runtime = Runtime.getRuntime();
                runtime.gc();
                long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
                statement.evaluate();
                runtime.gc();
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();

                if (memoryAfter - memoryBefore > limit) {
                    throw new Exception("memory leak");
                }
            }
        };
    }

    public void limit(long mb) {
        limit = mb;
    }
}
