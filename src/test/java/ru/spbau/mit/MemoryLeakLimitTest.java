package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakLimitTest {

    private static int[] buf;
    private static List<Integer> list = new ArrayList<>();

    @Rule
    public final MemoryLeakLimit memoryLeakLimit = new MemoryLeakLimit();


    @Test
    public void testFailed() {
        memoryLeakLimit.limit(1);
        buf = new int[1000000];
        for (Integer i = 0; i < 100000; ++i) {
            list.add(i);
        }
    }

    @Test
    public void testPassed() {
        memoryLeakLimit.limit(10);
        buf = new int[1024];
        for (int i = 0; i < 1024; ++i) {
            buf[i] = i;
        }
    }

    @Test(expected = Exception.class)
    public void testExceptionfromTest() throws Exception {
        memoryLeakLimit.limit(10);
        buf = new int[1024];
        throw new Exception("exception from test");
    }

    @Test(expected = Exception.class)
    public void testExceptionMemoryLeak() throws Exception {
        memoryLeakLimit.limit(1);
        buf = new int[1000000];

        for (Integer i = 0; i < 100000; ++i) {
            list.add(i);
        }
        throw new Exception("test");
    }
}
