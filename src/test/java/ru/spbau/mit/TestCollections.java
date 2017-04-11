package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCollections {

    @Test
    public void testMap() {
        Function1<Integer, Integer> addTen = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer argument) {
                return argument + 10;
            }
        };

        Integer[] array = {8, 9, 10, 11, 12};
        Integer[] correctlyMapped = {18, 19, 20, 21, 22};

        List<Integer> mapped = (List<Integer>) Collections.map(addTen, Arrays.asList(array));

        assertEquals(Arrays.asList(correctlyMapped), mapped);
    }

    @Test
    public void testMap1() {
        Function1<Object, String> stringify = new Function1<Object, String>() {
            @Override
            public String apply(Object argument) {
                return argument.toString();
            }
        };

        Integer[] array = {8, 9, 10, 11, 12};
        String[] correctlyMapped = {"8", "9", "10", "11", "12"};

        List<String> mapped = (List<String>) Collections.map(stringify, Arrays.asList(array));

        assertEquals(Arrays.asList(correctlyMapped), mapped);
    }

    @Test
    public void testFilter() {
        Integer[] array = {8, 9, 10, 11, 12};

        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        Integer[] correctlyFiltered = {11, 12};
        List<Integer> filtered = (List<Integer>) Collections.filter(moreThanTen, Arrays.asList(array));

        assertEquals(Arrays.asList(correctlyFiltered), filtered);
    }

    @Test
    public void testFilterAlwaysTrue() {
        Integer[] array = {8, 9, 10, 11, 12};

        List<Integer> filtered = (List<Integer>) Collections.filter(Predicate.ALWAYS_TRUE, Arrays.asList(array));

        assertEquals(Arrays.asList(array), filtered);
    }

    @Test
    public void testFilterAlwaysFalse() {
        Integer[] array = {8, 9, 10, 11, 12};

        List<Integer> filtered = (List<Integer>) Collections.filter(Predicate.ALWAYS_FALSE, Arrays.asList(array));

        assertTrue(filtered.isEmpty());
    }

    @Test
    public void testTakeWhile() {
        Integer[] array = {8, 9, 10, 7, 6};

        Predicate<Integer> lessThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg < 10;
            }
        };
        Integer[] correct = {8, 9};

        List<Integer> filtered = (List<Integer>) Collections.takeWhile(lessThanTen, Arrays.asList(array));

        assertEquals(Arrays.asList(correct), filtered);
    }

    @Test
    public void testTakeUnless() {
        Integer[] array = {8, 9, 11, 7, 6};

        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };
        Integer[] correct = {8, 9};

        List<Integer> filtered = (List<Integer>) Collections.takeUnless(moreThanTen, Arrays.asList(array));

        assertEquals(Arrays.asList(correct), filtered);
    }

    @Test
    public void testFoldL() {
        Integer[] array = {1, 2, 3, 4, 5};

        Function2<Integer, Integer, Integer> fun2 = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        };

        assertEquals(new Integer(25), Collections.foldl(fun2, 10, Arrays.asList(array)));
    }

    @Test
    public void testFoldR() {
        Integer[] array = {1, 2, 3, 4, 5};

        Function2<Integer, Integer, Integer> fun2 = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 * arg2;
            }
        };

        assertEquals(new Integer(120), Collections.foldl(fun2, 1, Arrays.asList(array)));
    }

    @Test
    public void testFoldLNonAssociative() {
        Integer[] array = {1, 2, 3, 4, 5};

        Function2<Integer, Integer, Integer> fun2 = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 - arg2;
            }
        };

        assertEquals(new Integer(-15), Collections.foldl(fun2, 0, Arrays.asList(array)));
    }

    @Test
    public void testFoldRNonAssociative() {
        Integer[] array = {1, 2, 3, 4, 5};

        Function2<Integer, Integer, Integer> fun2 = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 - arg2;
            }
        };

        assertEquals(new Integer(3), Collections.foldr(fun2, 0, Arrays.asList(array)));
    }
}
