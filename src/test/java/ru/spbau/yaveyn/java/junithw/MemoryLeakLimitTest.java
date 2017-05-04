package ru.spbau.yaveyn.java.junithw;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by bronti on 04.05.17.
 */
public class MemoryLeakLimitTest {

    @Rule
    public MemoryLeakLimit mll = new MemoryLeakLimit();



    @Test
    public void testSuccess() {
        mll.limit(1);
    }

    @Test
    @Ignore
    public void testFail() {
        mll.limit(-1);
    }
}
