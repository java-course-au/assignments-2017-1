package ru.spbau.mit;

import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import static org.junit.Assert.*;
import static ru.spbau.mit.TestUtils.*;

public class Function1Test {
    @Test
    public void testApply() {
        assertTrue(NOT.apply(false));
        assertFalse(NOT.apply(true));
    }

    @Test
    public void testCompose() {
        assertTrue(NOT.compose(NOT).apply(true));
        assertFalse(NOT.compose(NOT).apply(false));
    }

    @Test
    public void testComposeToString() {
        assertEquals(NOT.compose(TO_STRING).apply(false), "true");
        assertEquals(NOT.compose(TO_STRING).apply(true), "false");
    }

    /** Wildcard "tests" */

    @Test
    public void testComposeWildcard() {
        final Function1<A, C> function1 = new Function1<A, C>() {
            @Override
            C apply(@Nullable final A arg) {
                return null;
            }
        };

        final Function1<B, B> function2 = new Function1<B, B>() {
            @Override
            B apply(@Nullable final B arg) {
                return null;
            }
        };

        final B actual = function1.compose(function2).apply(new A());
    }
}
