package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFunction2 {

    @Test
    public void testBasicApply() {
        Function2<Integer, Double, String> concat = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        assertEquals("1233.14", concat.apply(123, 3.14));
    }

    @Test
    public void testBasicCompose() {
        Function2<Integer, Double, String> concat = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        Function1<String, Integer> getLength = new Function1<String, Integer>() {
            @Override
            public Integer apply(String arg) {
                return arg.length();
            }
        };

        assertEquals(new Integer(7), concat.compose(getLength).apply(123, 3.14));
    }

    @Test
    public void testBind1() {
        Function2<Integer, Double, String> concat = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        assertEquals("420.123", concat.bind1(42).apply(0.123));
    }

    @Test
    public void testBind2() {
        Function2<Integer, Double, String> fun2 = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        assertEquals("420.123", fun2.bind2(0.123).apply(42));
    }

    @Test
    public void testCurry() {
        final int arg1 = 42;
        final double arg2 = 0.123;

        Function2<Integer, Double, String> fun2 = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        assertEquals("420.123", fun2.curry().apply(arg1).apply(arg2));
    }
}
