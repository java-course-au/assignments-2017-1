package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {
    private long memLimit;

    public void limit(long mb) {
        final int twoInTen = 1024;
        memLimit = mb * twoInTen * twoInTen;
    }

    @Override
    public Statement apply(final Statement statement, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Runtime runtime = Runtime.getRuntime();

                runtime.gc();
                long before = runtime.totalMemory() - runtime.freeMemory();

                statement.evaluate();

                runtime.gc();
                long after = runtime.totalMemory() - runtime.freeMemory();

                assert after - before <= memLimit;
            }
        };
    }

}
