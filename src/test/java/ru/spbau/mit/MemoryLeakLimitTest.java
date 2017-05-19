package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakLimitTest {

    private final static int BUFFER_SIZE = 2 * 1024 * 1024;
    private byte[] buffer;
    private static List<Integer> listBuffer = new ArrayList<>();

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

    @Test
    public void memoryLeakExceptionTest() throws Exception {
        mlt.limit(1);
        buffer = new byte[BUFFER_SIZE];
        for(Integer i = 0; i < 10000; i++) {
            listBuffer.add(i);
        }
        throw new Exception();
    }

    @Test
    public void memoryLeakNonExceptionTest() throws Exception {
        mlt.limit(10);
        buffer = new byte[1024];
        throw new Exception("test");
    }
}