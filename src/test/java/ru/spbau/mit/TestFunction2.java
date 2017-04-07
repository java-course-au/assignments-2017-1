package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFunction2 {

    @Test
    public void testBasicApply() {
        final int arg1 = 123;
        final double arg2 = 3.14;

        Function2<Integer, Double, String> concat = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        assertEquals(concat.apply(arg1, arg2), "1233.14");
    }

    @Test
    public void testBasicCompose() {
        final int arg1 = 123;
        final double arg2 = 3.14;

        Function2<Integer, Double, String> fun2 = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        Function1<String, Integer> fun1 = new Function1<String, Integer>() {
            @Override
            public Integer apply(String arg) {
                return arg.length();
            }
        };

        assertEquals(fun2.compose(fun1).apply(arg1, arg2), new Integer((Integer.toString(arg1)
                + Double.toString(arg2)).length()));
    }

    @Test
    public void testBind1() {
        final int arg1 = 42;
        final double arg2 = 0.123;

        Function2<Integer, Double, String> fun2 = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        assertEquals(fun2.bind1(arg1).apply(arg2), "420.123");
    }

    @Test
    public void testBind2() {
        final int arg1 = 42;
        final double arg2 = 0.123;

        Function2<Integer, Double, String> fun2 = new Function2<Integer, Double, String>() {
            @Override
            public String apply(Integer arg1, Double arg2) {
                return arg1.toString() + arg2.toString();
            }
        };

        assertEquals(fun2.bind2(arg2).apply(arg1), "420.123");
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

        assertEquals(fun2.curry().apply(arg1).apply(arg2), "420.123");
    }
}
