package ru.spbau.mit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
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
    private static final Function2<Integer, Integer, Integer> MINUS_OP =
            new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer param1, Integer param2) {
            return param1 - param2;
        }
    };
    private static final Function2<Integer, Integer, Integer> PLUS_OP =
            new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer param1, Integer param2) {
            return param1 + param2;
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
        Function1<Number, Integer> inc = new Function1<Number, Integer>() {
            public Integer apply(Number param) {
                return param.intValue() + 1;
            }
        };

        final Integer[] expected = new Integer[] {2, 4, 3, 5, 8, 3, 6};
        assertEquals(Collections.map(inc, collection), Arrays.asList(expected));
    }

    @Test
    public void testFilter() {
        final Integer[] expected = new Integer[] {4, 7, 5};
        assertEquals(Collections.filter(GT_THRESHOLD_PRED, collection), Arrays.asList(expected));
    }

    @Test
    public void testTakeWhile() {
        final Integer[] expected = new Integer[] {1, 3, 2};
        final Iterable<Integer> res = Collections.takeWhile(GT_THRESHOLD_PRED.not(), collection);
        assertEquals(res, Arrays.asList(expected));
    }

    @Test
    public void testTakeUnless() {
        final Integer[] expected = new Integer[] {1, 3, 2};
        Iterable<Integer> res = Collections.takeUnless(GT_THRESHOLD_PRED, collection);
        assertEquals(res, Arrays.asList(expected));
    }

    @Test
    public void testFold() {
        int res = 0;
        for (Integer val : collection) {
            res += val;
        }
        assertEquals(new Integer(res), Collections.foldl(PLUS_OP, 0, collection));
        assertEquals(new Integer(res), Collections.foldr(PLUS_OP, 0, collection));
    }

    @Test
    public void testFoldlAssociativity() {
        int foldlRes = 0;
        for (Integer value : TEST_SAMPLE) {
            foldlRes -= value;
        }
        assertEquals(new Integer(foldlRes), Collections.foldl(MINUS_OP, 0, collection));
    }

    @Test
    public void testFoldrAssociativity() {
        int foldrRes = 0;
        for (int idx = TEST_SAMPLE.length - 1; idx >= 0; --idx) {
            foldrRes = TEST_SAMPLE[idx] - foldrRes;
        }
        assertEquals(new Integer(foldrRes), Collections.foldr(MINUS_OP, 0, collection));
    }
}
