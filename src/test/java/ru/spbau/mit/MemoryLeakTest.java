package ru.spbau.mit;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakTest {
    private static List<Integer> vec = new ArrayList<>();

    @Rule
    public MemoryLeakLimit leak = new MemoryLeakLimit();


    @Test
    public void memTest() {
        final int bigNumber = 10000000;
        leak.limit(1);

        for (Integer i = 0; i < bigNumber; i++) {
            vec.add(i);
        }
    }
}
