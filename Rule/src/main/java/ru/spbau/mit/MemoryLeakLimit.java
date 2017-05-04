package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {

    private static final long MB = 1024 * 1024;
    private long limit;

    public void limit(long limitMemory) {
        limit = limitMemory * MB;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Runtime runtime = Runtime.getRuntime();
                runtime.gc();
                long freeMemoryBefore = runtime.freeMemory();

                base.evaluate();

                runtime.gc();
                long freeMemoryAfter = runtime.freeMemory();

                if (freeMemoryAfter - freeMemoryBefore > limit) {
                    throw new Exception("leak memory");
                }
            }
        };
    }
}
