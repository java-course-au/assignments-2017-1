package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFunction1 {

    @Test
    public void testBasicApply() {
        final int num = 123;
        Function1<Integer, String> toStringFun = new Function1<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return integer.toString();
            }
        };

        assertEquals(toStringFun.apply(num), Integer.toString(num));
    }

    @Test
    public void testBasicCompose() {
        final double arg = 3.14;

        Function1<Double, String> doubleToStr = new Function1<Double, String>() {
            @Override
            public String apply(Double arg) {
                return arg.toString();
            }
        };

        Function1<String, Integer> strToInt = new Function1<String, Integer>() {
            @Override
            public Integer apply(String arg) {
                return arg.length();
            }
        };

        assertEquals(doubleToStr.compose(strToInt).apply(arg), new Integer(Double.toString(arg).length()));
    }

    @Test
    public void testCastedCompose() {
        Function1<Object, String> objectToStr = new Function1<Object, String>() {
            @Override
            public String apply(Object arg) {
                return arg.toString();
            }
        };

        Function1<String, Integer> strToInt = new Function1<String, Integer>() {
            @Override
            public Integer apply(String arg) {
                return arg.length();
            }
        };

        assertEquals(strToInt.compose(objectToStr).apply("lorem ipsum"), "11");
    }
}
