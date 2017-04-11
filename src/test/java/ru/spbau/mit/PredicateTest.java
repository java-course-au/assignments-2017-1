package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;
import static ru.spbau.mit.Function1Test.*;

public class PredicateTest {
    private Predicate<B> gtZeroPred = new Predicate<B>() {
        public Boolean apply(B param) {
            return param.get() > 0;
        }
    };

    @Test
    public void testAlwaysTrue() {
        assertTrue(gtZeroPred.ALWAYS_TRUE.apply(new C(2)));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(gtZeroPred.ALWAYS_FALSE.apply(new C(0)));
    }

    @Test
    public void testOr() {
        assertFalse(gtZeroPred.or(gtZeroPred.ALWAYS_FALSE).apply(new C(0)));
        assertTrue(gtZeroPred.or(gtZeroPred.ALWAYS_FALSE).apply(new C(1)));
    }

    @Test
    public void testAnd() {
        assertFalse(gtZeroPred.and(gtZeroPred.ALWAYS_FALSE).apply(new C(1)));
        assertTrue(gtZeroPred.and(gtZeroPred.ALWAYS_TRUE).apply(new C(1)));
    }

    @Test
    public void testNot() {
        assertFalse(gtZeroPred.not().apply(new C(1)));
        assertTrue(gtZeroPred.not().apply(new C(0)));
    }

    @Test
    public void testLaziness() {
        Predicate<A> throwPred = new Predicate<A>() {
            public Boolean apply(A param) {
                fail();
                return true;
            }
        };

        assertTrue(gtZeroPred.or(throwPred).apply(new C(1)));
        assertFalse(gtZeroPred.and(throwPred).apply(new C(0)));
    }
}
