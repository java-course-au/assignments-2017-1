package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static ru.spbau.mit.TestUtils.*;

public class CollectionsTest {
    @NotNull
    private static final List<Integer> NUMBERS = asList(0, 1, 2, 3, 4, 5);

    @Test
    public void testMap() {
        final Iterable<Integer> expected = asList(0, 2, 4, 6, 8, 10);
        final Iterable<? extends Integer> actual = Collections.map(DOUBLE_NUM, NUMBERS);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilter() {
        final Iterable<Integer> expected = asList(0, 2, 4);
        final Iterable<? extends Integer> actual = Collections.filter(IS_EVEN, NUMBERS);
        assertEquals(expected, actual);
    }

    @Test
    public void testTakeWhile() {
        final Predicate<Integer> isLessThan3 = new Predicate<Integer>() {
            private static final int THREE = 3;

            @Nullable
            @Override
            Boolean apply(@Nullable final Integer arg) {
                return arg != null ? arg < THREE : null;
            }
        };

        final Iterable<Integer> expected = asList(0, 1, 2);
        final Iterable<? extends Integer> actual = Collections.takeWhile(isLessThan3, NUMBERS);
        assertEquals(expected, actual);
    }

    @Test
    public void testTakeUnless() {
        final Predicate<Integer> isGreaterThan2 = new Predicate<Integer>() {
            private static final int TWO = 2;

            @Nullable
            @Override
            Boolean apply(@Nullable final Integer arg) {
                return arg != null ? arg > TWO : null;
            }
        };

        final Iterable<Integer> expected = asList(0, 1, 2);
        final Iterable<? extends Integer> actual = Collections.takeUnless(isGreaterThan2, NUMBERS);
        assertEquals(expected, actual);
    }

    @Test
    public void testFoldl() {
        final Integer expected = -15;
        final Integer actual = Collections.foldl(SUBTRACTION, 0, NUMBERS);
        assertEquals(expected, actual);
    }

    @Test
    public void testFoldr() {
        final Integer expected = -3;
        final Integer actual = Collections.foldr(SUBTRACTION, 0, NUMBERS);
        assertEquals(expected, actual);
    }

    /** Wildcard "tests" */

    @Test
    public void testMapWildcard() {
        final List<B> actual = Collections.map(new Function1<A, B>() {
            @Nullable
            @Override
            C apply(@Nullable final A arg) {
                return null;
            }
        }, singletonList(new B()));
    }

    @Test
    public void testFilterWildcard() {
        final List<B> actual = Collections.filter(new Predicate<A>() {
            @NotNull
            @Override
            Boolean apply(@Nullable final A arg) {
                return false;
            }
        }, singletonList(new B()));
    }

    @Test
    public void testTakeWhileWildcard() {
        final List<B> actual = Collections.takeWhile(new Predicate<A>() {
            @NotNull
            @Override
            Boolean apply(@Nullable final A arg) {
                return false;
            }
        }, singletonList(new B()));
    }

    @Test
    public void testTakeUnlessWildcard() {
        final List<B> actual = Collections.takeUnless(new Predicate<A>() {
            @NotNull
            @Override
            Boolean apply(@Nullable final A arg) {
                return false;
            }
        }, singletonList(new B()));
    }

    @Test
    public void testFoldlWildcard() {
        final B actual = Collections.foldl(new Function2<A, A, C>() {
            @Nullable
            @Override
            C apply(@Nullable final A arg1, @Nullable final A arg2) {
                return null;
            }
        }, new B(), singletonList(new C()));
    }

    @Test
    public void testFoldrWildcard() {
        final B actual = Collections.foldr(new Function2<A, A, C>() {
            @Nullable
            @Override
            C apply(@Nullable final A arg1, @Nullable final A arg2) {
                return null;
            }
        }, new B(), singletonList(new B()));
    }
}
