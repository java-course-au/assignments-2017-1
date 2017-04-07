package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
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
}
