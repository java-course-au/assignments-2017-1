package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {
    private static final int BYTES_IN_KB = 1024;
    private long bytes;

    void limit(long mb) {
        this.bytes = mb * BYTES_IN_KB * BYTES_IN_KB;
    }

    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final Runtime runtime = Runtime.getRuntime();

                runtime.gc();
                long before = runtime.totalMemory() - runtime.freeMemory();

                base.evaluate();

                runtime.gc();
                long after = runtime.totalMemory() - runtime.freeMemory();

                if (after - before > bytes) {
                    throw new Exception("Memory leak limit");
                }
            }
        };
    }
}
