package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayDeque;

import static org.junit.Assert.*;

public class PredicateTest {
    @Test
    public void testAlways() {
        assertTrue(Predicate.ALWAYS_TRUE.apply("31"));
        assertFalse(Predicate.ALWAYS_FALSE.apply(new ArrayDeque<>()));
    }

    @Test
    public void testCompositions() {
        Predicate<Integer> eq31 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg == 31;
            }
        };

        Predicate<Object> fail = new Predicate<Object>() {
            @Override
            public Boolean apply(Object arg) {
                throw new AssertionError();
            }
        };

        assertTrue(eq31.or(fail).apply(31));
        assertFalse(eq31.and(fail).apply(30));
        assertFalse(Predicate.ALWAYS_TRUE.not().apply("hello"));

        Predicate<Integer> mod3 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 3 == 0;
            }
        };

        Predicate<Object> longEnough = new Predicate<Object>() {
            @Override
            public Boolean apply(Object arg) {
                return arg.toString().length() > 1;
            }
        };


        assertTrue(mod3.or(longEnough).apply(10));
        assertFalse(mod3.or(longEnough).apply(5));
        assertFalse(mod3.and(longEnough).apply(9));
        assertTrue(mod3.and(longEnough).apply(15));
    }
}
