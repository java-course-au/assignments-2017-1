package ru.spbau.mit;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


public class MemoryLeakLimit implements TestRule {



    private long limit = 0;

    void limit(long mb) {
        this.limit = 1024 * 1024 * mb;
    }

    @Override
    public Statement apply(final Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                Runtime runtime = Runtime.getRuntime();

                runtime.gc();
                long usedBefore = runtime.totalMemory() - runtime.freeMemory();

                Throwable catchedFromEvaluation = null;

                try {
                    statement.evaluate();
                } catch (Throwable th) {
                    catchedFromEvaluation = th;
                }


                runtime.gc();
                long usedAfter = runtime.totalMemory() - runtime.freeMemory();

                if (usedAfter - usedBefore > limit) {
                    throw new AssumptionViolatedException("memory leak limit exceeded");
                }

                if (catchedFromEvaluation != null) {
                    throw catchedFromEvaluation;
                }
            }
        };
    }
}
