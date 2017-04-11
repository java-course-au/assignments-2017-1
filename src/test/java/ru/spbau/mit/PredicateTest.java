package ru.spbau.mit;

import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import static org.junit.Assert.*;
import static ru.spbau.mit.TestUtils.*;

public class PredicateTest {
    @Test
    public void testOr() {
        assertTrue(IS_TRUE.or(ALWAYS_THROW).apply(true));
        assertTrue(IS_FALSE.or(IS_TRUE).apply(true));
        assertFalse(IS_FALSE.or(IS_FALSE).apply(true));
    }

    @Test
    public void testAnd() {
        assertFalse(IS_FALSE.and(ALWAYS_THROW).apply(true));
        assertTrue(IS_TRUE.and(IS_TRUE).apply(true));
        assertFalse(IS_TRUE.and(IS_FALSE).apply(true));
    }

    @Test
    public void testNot() {
        assertTrue(IS_FALSE.not().apply(true));
        assertFalse(IS_TRUE.not().apply(true));
    }

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicate.ALWAYS_TRUE.apply(null));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicate.ALWAYS_FALSE.apply(null));
    }

    /** Wildcard "tests" */

    @Test
    public void testOrAndWildcard() {
        final Predicate<B> predicate1 = new Predicate<B>() {
            @Nullable
            @Override
            Boolean apply(@Nullable final B arg) {
                return null;
            }
        };

        final Predicate<A> predicate2 = new Predicate<A>() {
            @Nullable
            @Override
            Boolean apply(@Nullable final A arg) {
                return null;
            }
        };

        final Predicate<B> actual1 = predicate1.or(predicate2);
        final Predicate<B> actual2 = predicate1.and(predicate2);
    }
}
