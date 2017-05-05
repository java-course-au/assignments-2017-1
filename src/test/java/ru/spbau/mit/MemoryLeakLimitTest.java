package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;

public class MemoryLeakLimitTest {

    private final static int BUFFER_SIZE = 2 * 1024 * 1024;
    private byte[] buffer;

    @Rule
    public final MemoryLeakLimit mlt = new MemoryLeakLimit();

    @Test
    public void emptyTest() {
        mlt.limit(0);
    }

    @Test
    public void limitFailTest() {
        mlt.limit(1);
        buffer = new byte[BUFFER_SIZE];
    }

    @Test
    public void limitPassTest() {
        mlt.limit(2);
        buffer = new byte[BUFFER_SIZE];
    }
}