package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakLimitTest {

    private static List<Integer> list = new ArrayList<>();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public MemoryLeakLimit leakLimitRule = new MemoryLeakLimit();

    @Test
    public void testMemoryLeakLimitFail() { // must fail
        memoryLeak();
    }

    @Test
    public void testMemoryLeakWithException() { // must fail
        thrown.expect(NullPointerException.class);
        memoryLeak();
        throw new NullPointerException();
    }




    private void memoryLeak() {
        leakLimitRule.limit(1);
        final int numberOfInts = 1024 * 1024 * 5;
        for (int i = 0; i < numberOfInts; i++) {
            list.add(1000);
        }
    }
}
