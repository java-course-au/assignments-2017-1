package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class Functional1Test {
    @Test
    public void testApply() throws Exception {
        Functional1<Integer, Integer> v = new Functional1<Integer, Integer>() {
            @Override
            Integer apply(Integer integer) {
                final int c = 4;
                return integer * c;
            }
        };
        final Integer check = 16;
        final Integer param = 4;
        assertEquals(v.apply(param), check);
    }

    @Test
    public void testCompose() throws Exception {
        Functional1<Integer, Double> v = new Functional1<Integer, Double>() {
            @Override
            Double apply(Integer i) {
                return i.doubleValue();
            }
        };
        Functional1<Double, Double> u = new Functional1<Double, Double>() {
            @Override
            Double apply(Double d) {
                final double c = 4.0;
                return d * c;
            }
        };
        final Double check = 16.0;
        final Integer param = 4;
        assertEquals(v.compose(u).apply(param), check);
    }
}
