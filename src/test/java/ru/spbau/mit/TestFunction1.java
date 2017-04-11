package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFunction1 {

    @Test
    public void testBasicApply() {
        Function1<Integer, String> toStringFun = new Function1<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return integer.toString();
            }
        };

        assertEquals("123", toStringFun.apply(123));
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

        assertEquals(new Integer(Double.toString(arg).length()), doubleToStr.compose(strToInt).apply(arg));
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

        assertEquals("11", strToInt.compose(objectToStr).apply("lorem ipsum"));
    }
}
