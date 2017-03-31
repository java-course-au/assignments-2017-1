package ru.spbau.mit;

import static org.junit.Assert.*;
import org.junit.Test;

public class Function1Test {
    @Test
    public void testSucc() {
        Integer x = 30;
        Integer sx = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg + 1;
            }
        }.apply(x);
        assertEquals(sx, Integer.valueOf(31));
    }

    @Test
    public void testCompose() {
        Function1<Integer, Integer> mul2 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg * 2;
            }
        };

        Function1<Integer, Integer> add2 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg + 2;
            }
        };

        assertEquals(mul2.compose(add2).apply(2), Integer.valueOf(6));
        assertEquals(add2.compose(mul2).apply(2), Integer.valueOf(8));
    }

    @Test
    public void testComposeCast() {
        Function1<Integer, Double> f = new Function1<Integer, Double>() {
            @Override
            public Double apply(Integer arg) {
                return arg.doubleValue();
            }
        };

        Function1<Number, String> g = new Function1<Number, String>() {
            @Override
            public String apply(Number arg) {
                return arg.toString();
            }
        };

        assertEquals(f.compose(g).apply(31), "31.0");
    }
}
