package ru.spbau.yaveyn.java.junithw;

/**
 * Created by bronti on 04.05.17.
 */

import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MemoryLeakLimit implements TestRule {
    private long mbLimit;

    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Runtime rt = Runtime.getRuntime();
                System.gc();
                long beforeUsedMb = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
                base.evaluate();
                long afterUsedMb = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
                System.gc();
                Assert.assertTrue(
                        String.format("Memory leak detected (%1$d Mb > %2$d Mb)", afterUsedMb - beforeUsedMb, mbLimit),
                        afterUsedMb - beforeUsedMb <= mbLimit);
            }
        };
    }

    public void limit(long mb) {
        this.mbLimit = mb;
    }
}
