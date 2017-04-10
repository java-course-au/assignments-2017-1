package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.*;

public class Function2Test {
    @Test
    public void testSum() {
        Function2<Integer, Integer, Integer> f = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        };
        assertEquals(f.apply(15, 16), Integer.valueOf(31));
    }

    @Test
    public void testCompose() {
        Function1<Integer, Integer> add1 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg + 1;
            }
        };
        Function2<Integer, Integer, Integer> mul = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 * arg2;
            }
        };

        assertEquals(mul.compose(add1).apply(15, 2), Integer.valueOf(31));
    }

    @Test
    public void testBind() {
        Function2<Integer, Integer, Integer> sub = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 - arg2;
            }
        };

        Function1<Integer, Integer> sub1 = sub.bind2(1);
        Function1<Integer, Integer> subFrom1 = sub.bind1(1);

        assertEquals(sub1.apply(31), Integer.valueOf(30));
        assertEquals(subFrom1.apply(31), Integer.valueOf(-30));
    }

    @Test
    public void testCurry() {
        Function1<Integer, Function1<Integer, Integer>> curried = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        }.curry();

        assertEquals(curried.apply(15).apply(16), Integer.valueOf(31));
    }

    @Test
    public void testFlip() {
        Function2<Integer, Integer, Integer> flipped = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 - arg2;
            }
        }.flip();

        assertEquals(flipped.apply(15, 16), Integer.valueOf(1));
    }
}
