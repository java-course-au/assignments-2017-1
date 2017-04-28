package ru.spbau.mit;

import org.junit.Rule;
import org.junit.Test;

public class MemLeakTest {
    private static final int LARGE_BUFFER_SIZE = 2 * 1024 * 1024;
    private static Object dump;

    @Rule
    public final MemoryLeakLimit leakChecker = new MemoryLeakLimit();

    @Test
    public void leakTest() {
        leakChecker.limit(1);
        dump = new Object[LARGE_BUFFER_SIZE];
    }

    @Test
    public void simpleTest() {
        leakChecker.limit(1);
        dump = new Object[1];
    }
}
