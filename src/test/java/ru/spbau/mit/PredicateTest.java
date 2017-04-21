package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class PredicateTest {
    private static final Predicate<Integer> FIRST = new Predicate<Integer>() {
        @Override
        Boolean apply(Integer s) {
            return s == 0;
        }
    };
    private static final Predicate<Integer> SECOND = new Predicate<Integer>() {
        @Override
        Boolean apply(Integer s) {
            return s / 0 == 0;
        }
    };
    @Test
    public void testApply() throws Exception {
        Predicate<String> u = new Predicate<String>() {
            @Override
            Boolean apply(String s) {
                return s.length() > 0;
            }
        };
        assertTrue(u.apply("Test String"));
        assertFalse(u.apply(""));
        assertEquals(u.apply("Test string"), Predicate.ALWAYS_TRUE.apply(null));
        assertEquals(u.apply(""), Predicate.ALWAYS_FALSE.apply(null));
    }

    @Test
    public void testNot() throws Exception {
        Predicate<String> u = new Predicate<String>() {
            @Override
            Boolean apply(String s) {
                return s.length() > 0;
            }
        };
        assertFalse(u.not().apply("Test String"));
        assertTrue(u.not().apply(""));
    }

    @Test
    public void testOr() throws Exception {
        Predicate<Integer> u = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer s) {
                return s == 10;
            }
        };
        Predicate<Integer> v = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer s) {
                return s == 5;
            }
        };
        Predicate<Integer> or = u.or(v);
        assertTrue(or.apply(5));
        assertTrue(or.apply(10));
        assertFalse(or.apply(6));

    }

    @Test
    public void testOrLazy() throws Exception {
        Predicate<Integer> or = FIRST.or(SECOND);
        assertTrue(or.apply(0));
    }
    @Test
    public void testAnd() throws Exception {
        Predicate<String> u = new Predicate<String>() {
            @Override
            Boolean apply(String s) {
                return s.length() > 0;
            }
        };
        Predicate<String> v = new Predicate<String>() {
            @Override
            Boolean apply(String s) {
                return s.length() <= 11;
            }
        };
        Predicate<String> and = u.and(v);
        assertTrue(and.apply("Test string"));
        assertFalse(u.and(v).apply("Test   string"));
        and = v.and(u);
        assertTrue(and.apply("Test string"));
        assertFalse(u.and(v).apply(""));
    }
    @Test
    public void testAndLazy() throws Exception {
        Predicate<Integer> and = FIRST.and(SECOND);
        assertFalse(and.apply(10));
    }


}
