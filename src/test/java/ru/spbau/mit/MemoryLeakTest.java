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
    public void memTestFail() throws Exception {
        final int bigNumber = 10000000;
        leak.limit(1);

        for (Integer i = 0; i < bigNumber; i++) {
            vec.add(i);
        }
    }

    @Test
    public void memTestPass() throws Exception {
        final int smallNumber = 10;
        leak.limit(1);

        for (Integer i = 0; i < smallNumber; i++) {
            vec.add(i);
        }
    }

    @Test(expected = Exception.class)
    public void memTestExpectedExceptionMemFail() throws Exception {
        final int bigNumber = 10000000;
        leak.limit(1);

        for (Integer i = 0; i < bigNumber; i++) {
            vec.add(i);
        }

        throw new Exception();
    }

    @Test(expected = Exception.class)
    public void memTestExpectedExceptionButNotMemFail() throws Exception {
        final int smallNumber = 10;
        leak.limit(1);

        for (Integer i = 0; i < smallNumber; i++) {
            vec.add(i);
        }

        throw new Exception("Test");
    }
}
