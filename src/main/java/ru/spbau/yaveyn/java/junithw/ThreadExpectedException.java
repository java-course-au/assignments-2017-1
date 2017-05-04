package ru.spbau.yaveyn.java.junithw;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bronti on 04.05.17.
 */
public class ThreadExpectedException implements TestRule {

    private Class<? extends Throwable> expectedThrowable;
    private Queue<Thread> registeredThreads = new ConcurrentLinkedQueue<>();
    private final Queue<Boolean> threadCorectness = new ConcurrentLinkedQueue<>();

    public void expect(Class<? extends Throwable> e){
        expectedThrowable = e;
    }

    public void registerThread(Thread t) {
        registeredThreads.add(t);
        final Class<? extends Throwable> currentThrowable = expectedThrowable;
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (currentThrowable.equals(e.getClass())) {
                    threadCorectness.add(true);
                }
            }
        });
//        t.start();
        // не уверена, что правильно поняла задание, если честно..
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
                String msg = "Some thread is running or did not throw expected exception.";
                if (expectedThrowable != null) {
                    Assert.assertEquals(msg, registeredThreads.size(), threadCorectness.size());
                }
                for (Thread t : registeredThreads) {
                    t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {}
                    });
                }
                registeredThreads.clear();
                threadCorectness.clear();
                // судя по заданию, если expectedThrowable -- null, то не надо ничего проверять. Надеюсь, я правильно поняла.
            }
        };
    }
}
