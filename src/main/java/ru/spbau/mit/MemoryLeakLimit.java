package ru.spbau.mit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {
    public final static long MULT = 1024 * 1024;

    private long limit;

    @Override
    public Statement apply(final Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final long before = getOccupiedMemory();
                Throwable e = null;
                try {
                    statement.evaluate();
                } catch (Throwable ex) {
                    e = ex;
                }
                final long after = getOccupiedMemory();
                if (after - before > limit) {
                    throw new Exception("Exception: memory limit");
                }
                if (e != null) {
                    throw e;
                }
            }
        };
    }

    private long getOccupiedMemory() {
        final Runtime rt = Runtime.getRuntime();
        rt.gc();
        return rt.totalMemory() - rt.freeMemory();
    }

    public void limit(long mb) {
        limit = mb * MULT;
    }
}
