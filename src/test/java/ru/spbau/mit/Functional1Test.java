package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class Functional1Test {
    @Test
    public void testApply() throws Exception {
        Functional1<Integer, Integer> v = new Functional1<Integer, Integer>() {
            @Override
            Integer apply(Integer integer) {
                return integer * 4;
            }
        };
        assertEquals(v.apply(4), Integer.valueOf(16));
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
                return d * 4.0;
            }
        };
        assertEquals(v.compose(u).apply(4), Double.valueOf(16.0));
    }
}
