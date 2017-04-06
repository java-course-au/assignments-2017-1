package ru.spbau.mit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionsTest {

    private static final int THRESHOLD_VALUE = 3;

    private static final Integer[] TEST_SAMPLE = new Integer[] {1, 3, 2, 4, 7, 2, 5};
    private static final Predicate<Number> GT_THRESHOLD_PRED = new Predicate<Number>() {
        public Boolean apply(Number param) {
            return param.intValue() > THRESHOLD_VALUE;
        }
    };
    private List<Integer> collection;

    @Before
    public void setUp() {
        collection = Arrays.asList(TEST_SAMPLE);
    }

    @After
    public void tearDown() {
        collection = null;
    }

    @Test
    public void testMap() {
        Function1<Integer, Number> inc = new Function1<Integer, Number>() {
            public Integer apply(Number param) {
                return param.intValue() + 1;
            }
        };

        final Integer[] expected = new Integer[] {2, 4, 3, 5, 8, 3, 6};
        assertTrue(compareArrays(Collections.map(inc, collection), Arrays.asList(expected)));
    }

    @Test
    public void testFilter() {
        final Integer[] expected = new Integer[] {4, 7, 5};
        assertTrue(compareArrays(Collections.filter(GT_THRESHOLD_PRED, collection), Arrays.asList(expected)));
    }

    @Test
    public void testTakeWhile() {
        final Integer[] expected = new Integer[] {1, 3, 2};
        final Iterable<Integer> res = Collections.takeWhile(GT_THRESHOLD_PRED.not(), collection);
        assertTrue(compareArrays(res, Arrays.asList(expected)));
    }

    @Test
    public void testTakeUnless() {
        final Integer[] expected = new Integer[] {1, 3, 2};
        Iterable<Integer> res = Collections.takeUnless(GT_THRESHOLD_PRED, collection);
        assertTrue(compareArrays(res, Arrays.asList(expected)));
    }

    private static <T> boolean compareArrays(Iterable<T> c1, Iterable<T> c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1 == null || c2 == null) {
            return false;
        }

        final Iterator<T> it2 = c2.iterator();
        for (T item : c1) {
            if (!it2.hasNext() || !item.equals(it2.next())) {
                return false;
            }
        }
        return true;
    }
}
