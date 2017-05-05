package ru.spbau.mit;


import org.junit.Rule;
import org.junit.Test;

public class MemoryLeakLimitTest {

    private static int[] buf;

    @Rule
    public final MemoryLeakLimit memoryLeakLimit = new MemoryLeakLimit();


    @Test()
    public void test1() {
        memoryLeakLimit.limit(1);
        buf = new int[1024];
    }

    @Test()
    public void test2() {
        memoryLeakLimit.limit(1000);
        buf = new int[10];
    }
}
