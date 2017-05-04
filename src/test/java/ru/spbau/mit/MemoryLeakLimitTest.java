package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakLimitTest {

    private static List<Integer> list = new ArrayList<>();

    @Rule
    public MemoryLeakLimit leakLimitRule = new MemoryLeakLimit();

    @Test
    public void testMemoryLeakLimitFail() {
        leakLimitRule.limit(2);
        final int numberOfInts = 1024 * 1024;
        for (int i = 0; i < numberOfInts; i++) {
            list.add(1000);
        }
    }
}
