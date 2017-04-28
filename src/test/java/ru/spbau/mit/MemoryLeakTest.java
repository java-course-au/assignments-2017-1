package ru.spbau.mit;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakTest {
    @Rule
    public MemoryLeakLimit limit = new MemoryLeakLimit();

    public static List<int[]> data = null;

    // supposed to fail
    @Test
    public void testMemoryLeak() {
        limit.limit(1);
        data = new ArrayList<>();
        for (int i = 0; i < 1e4; i++) {
            data.add(new int[1024]);
        }
    }
}
