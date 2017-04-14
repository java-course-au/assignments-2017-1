package ru.spbau.mit;

import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CollectionsTest {
    private static final Integer[] SAMPLE = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
        @Override
        Integer apply(Integer first, Integer second) {
            return first - second;
        }
    };

    @Test
    public void testMap() throws Exception {
        Functional1<Integer, Integer> pow = new Functional1<Integer, Integer>() {
            @Override
            Integer apply(Integer integer) {
                return integer * integer;
            }
        };
        final Integer[] check = new Integer[] {0, 1, 4, 9, 16, 25, 36, 49, 64, 81};
        assertEquals(Collections.map(pow, Arrays.asList(SAMPLE)), Arrays.asList(check));
    }

    @Test
    public void testFilter() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                final int d = 2;
                return integer % d == 0;
            }
        };
        final Integer[] check = new Integer[] {0, 2, 4, 6, 8};
        assertEquals(Collections.filter(f, Arrays.asList(SAMPLE)), Arrays.asList(check));
    }

    @Test
    public void testTakeWhile() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                final int d = 5;
                return integer < d;
            }
        };
        final Integer[] check = new Integer[] {0, 1, 2, 3, 4};
        assertEquals(Collections.takeWhile(f, Arrays.asList(SAMPLE)), Arrays.asList(check));
    }

    @Test
    public void testTakeWhileNull() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                final int d = 0;
                return integer < d;
            }
        };
        assertEquals(Collections.takeWhile(f, Arrays.asList(SAMPLE)), java.util.Collections.emptyList());
    }

    @Test
    public void testTakeUnless() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                final int d = 5;
                return integer > d;
            }
        };
        final Integer[] check = new Integer[] {0, 1, 2, 3, 4, 5};
        assertEquals(Collections.takeUnless(f, Arrays.asList(SAMPLE)), Arrays.asList(check));
    }

    @Test
    public void testFoldl() throws Exception {
        Integer check = -45;
        assertEquals(Collections.foldl(u, 0, Arrays.asList(SAMPLE)), check);
    }

    @Test
    public void testFoldr() throws Exception {
        Integer check = -5;
        assertEquals(Collections.foldr(u, 0, Arrays.asList(SAMPLE)), check);
    }
}
