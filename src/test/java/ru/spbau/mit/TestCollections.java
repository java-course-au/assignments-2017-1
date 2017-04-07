package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestCollections {
    private static final int COL_SIZE = 10;

    @Test
    public void testMap() {
        final int addend = 10;
        Function1<Integer, Integer> addTen = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer argument) {
                return argument + addend;
            }
        };

        List<Integer> collection = new ArrayList<>();
        for (int i = 0; i < COL_SIZE; i++) {
            collection.add(i);
        }

        collection = (List) Collections.map(addTen, collection);

        for (int i = 0; i < COL_SIZE; i++) {
            assertEquals(new Integer(i + addend), collection.get(i));
        }
    }

    @Test
    public void testMap1() {
        Function1<Object, String> stringify = new Function1<Object, String>() {
            @Override
            public String apply(Object argument) {
                return argument.toString();
            }
        };

        List<Integer> collection = new ArrayList<>();
        for (int i = 0; i < COL_SIZE; i++) {
            collection.add(i);
        }

        List<String> newCollection = (List) Collections.map(stringify, collection);

        for (int i = 0; i < COL_SIZE; i++) {
            assertEquals(Integer.toString(i), newCollection.get(i));
        }
    }

    @Test
    public void testFilter() {
        Integer[] array = {8, 9, 10, 11, 12};
        final int numberOfElementsMoreThanTen = 2;

        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        List<Integer> filtered = (List<Integer>) Collections.filter(moreThanTen, Arrays.asList(array));

        assertEquals(filtered.size(), numberOfElementsMoreThanTen);
        assertEquals(filtered.get(0), array[3]);
        assertEquals(filtered.get(1), array[4]);
    }

    @Test
    public void testFilterAlwaysTrue() {
        Integer[] array = {8, 9, 10, 11, 12};

        List<Integer> filtered = (List<Integer>) Collections.filter(Predicate.ALWAYS_TRUE, Arrays.asList(array));

        assertEquals(filtered.size(), array.length);
        assertEquals(filtered.get(0), array[0]);
        assertEquals(filtered.get(1), array[1]);
        assertEquals(filtered.get(2), array[2]);
        assertEquals(filtered.get(3), array[3]);
        assertEquals(filtered.get(4), array[4]);
    }

    @Test
    public void testFilterAlwaysFalse() {
        Integer[] array = {8, 9, 10, 11, 12};

        List<Integer> filtered = (List<Integer>) Collections.filter(Predicate.ALWAYS_FALSE, Arrays.asList(array));

        assertEquals(filtered.size(), 0);
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

        List<Integer> filtered = (List<Integer>) Collections.takeWhile(lessThanTen, Arrays.asList(array));

        assertEquals(filtered.get(0), array[0]);
        assertEquals(filtered.get(1), array[1]);
    }

    @Test
    public void testTakeUnless() {
        Integer[] array = {8, 9, 10, 7, 6};

        Predicate<Integer> moreThanTen = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg > 10;
            }
        };

        List<Integer> filtered = (List<Integer>) Collections.takeUnless(moreThanTen, Arrays.asList(array));

        assertEquals(filtered.get(0), array[0]);
        assertEquals(filtered.get(1), array[1]);
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

        Collections.foldl(fun2, 10, Arrays.asList(array));

        assertEquals(Collections.foldl(fun2, 10, Arrays.asList(array)), new Integer(25));
    }
}
