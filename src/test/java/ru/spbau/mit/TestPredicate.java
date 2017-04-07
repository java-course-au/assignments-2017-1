package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPredicate {

    @Test
    public void testBasicPredicate() {
        final int value = 10;

        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > value;
            }
        };

        assertTrue(moreThanTen.apply(value + 1));
        assertFalse(moreThanTen.apply(value));
        assertFalse(moreThanTen.apply(value - 1));
    }

    @Test
    public void testAndPredicate() {
        final int value1 = 10;
        final int value2 = 20;
        final int diff = 5;

        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > value1;
            }
        };

        Predicate<Integer> lessThanTwenty = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg < value2;
            }
        };

        assertTrue(moreThanTen.and(lessThanTwenty).apply(value2 - diff));
        assertFalse(moreThanTen.and(lessThanTwenty).apply(value1 - diff));
        assertFalse(moreThanTen.and(lessThanTwenty).apply(value2 + diff));
    }

    @Test
    public void testOrPredicate() {
        final int value1 = 10;
        final int value2 = 20;
        final int diff = 5;

        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > value1;
            }
        };

        Predicate<Integer> moreThanTwenty = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > value2;
            }
        };

        assertTrue(moreThanTen.or(moreThanTwenty).apply(value2 - diff));
        assertFalse(moreThanTen.or(moreThanTwenty).apply(value1 - diff));
        assertTrue(moreThanTen.or(moreThanTwenty).apply(value2 + diff));
    }

    @Test
    public void testNotPredicate() {
        final int value1 = 10;

        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > value1;
            }
        };

        assertFalse(moreThanTen.not().apply(value1 + 1));
        assertTrue(moreThanTen.not().apply(value1));
        assertTrue(moreThanTen.not().apply(value1 - 1));
    }
}
