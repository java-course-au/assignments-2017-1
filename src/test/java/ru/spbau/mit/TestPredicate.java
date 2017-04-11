package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestPredicate {

    @Test
    public void testBasicPredicate() {
        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        assertTrue(moreThanTen.apply(11));
        assertFalse(moreThanTen.apply(10));
        assertFalse(moreThanTen.apply(9));
    }

    @Test
    public void testAndPredicate() {
        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        Predicate<Integer> lessThanTwenty = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg < 20;
            }
        };

        assertTrue(moreThanTen.and(lessThanTwenty).apply(15));
        assertFalse(moreThanTen.and(lessThanTwenty).apply(5));
        assertFalse(moreThanTen.and(lessThanTwenty).apply(25));
    }

    @Test
    public void testOrPredicate() {
        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        Predicate<Integer> moreThanTwenty = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 20;
            }
        };

        assertTrue(moreThanTen.or(moreThanTwenty).apply(15));
        assertFalse(moreThanTen.or(moreThanTwenty).apply(5));
        assertTrue(moreThanTen.or(moreThanTwenty).apply(25));
    }

    @Test
    public void testNotPredicate() {
        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        assertFalse(moreThanTen.not().apply(11));
        assertTrue(moreThanTen.not().apply(10));
        assertTrue(moreThanTen.not().apply(9));
    }

    @Test
    public void testLazyOr() {
        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        assertTrue(moreThanTen.or(null).apply(15));
    }

    @Test
    public void testLazyOr1() {
        final int[] indicator = {0};
        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        Predicate<Integer> lessThanTwenty = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                indicator[0] += 1;
                return arg < 20;
            }
        };

        assertTrue(moreThanTen.or(lessThanTwenty).apply(15));
        assertEquals(0, indicator[0]);

        assertTrue(moreThanTen.or(lessThanTwenty).apply(7));
        assertEquals(1, indicator[0]);


    }

    @Test
    public void testLazyAnd() {
        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        assertFalse(moreThanTen.and(null).apply(7));
    }

    @Test
    public void testLazyAnd1() {
        final int[] indicator = {0};
        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        Predicate<Integer> lessThanTwenty = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                indicator[0] += 1;
                return arg < 20;
            }
        };

        assertFalse(moreThanTen.and(lessThanTwenty).apply(7));
        assertEquals(0, indicator[0]);

        assertTrue(moreThanTen.and(lessThanTwenty).apply(15));
        assertEquals(1, indicator[0]);
    }
}
