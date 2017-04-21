package ru.spbau.mit;

import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CollectionsTest {
    private static final Integer[] TEST_SAMPLE = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final Functional2<Integer, Integer, Integer> SUBTRACTION =
            new Functional2<Integer, Integer, Integer>() {
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
        assertEquals(Collections.map(pow, Arrays.asList(TEST_SAMPLE)), Arrays.asList(check));
    }

    @Test
    public void testFilter() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        final Integer[] check = new Integer[] {0, 2, 4, 6, 8};
        assertEquals(Collections.filter(f, Arrays.asList(TEST_SAMPLE)), Arrays.asList(check));
    }

    @Test
    public void testTakeWhile() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                return integer < 5;
            }
        };
        final Integer[] check = new Integer[] {0, 1, 2, 3, 4};
        assertEquals(Collections.takeWhile(f, Arrays.asList(TEST_SAMPLE)), Arrays.asList(check));
    }

    @Test
    public void testTakeWhileNull() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                return integer < 0;
            }
        };
        assertEquals(Collections.takeWhile(f, Arrays.asList(TEST_SAMPLE)), java.util.Collections.emptyList());
    }

    @Test
    public void testTakeUnless() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                return integer > 5;
            }
        };
        final Integer[] check = new Integer[] {0, 1, 2, 3, 4, 5};
        assertEquals(Collections.takeUnless(f, Arrays.asList(TEST_SAMPLE)), Arrays.asList(check));
    }

    @Test
    public void testFoldl() throws Exception {
        assertEquals(Collections.foldl(SUBTRACTION, 0, Arrays.asList(TEST_SAMPLE)), Integer.valueOf(-45));
    }

    @Test
    public void testFoldr() throws Exception {
        assertEquals(Collections.foldr(SUBTRACTION, 0, Arrays.asList(TEST_SAMPLE)), Integer.valueOf(-5));
    }
}
