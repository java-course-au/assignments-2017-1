package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CollectionsTest {
    @Test
    public void testMap() {
        Function1<Object, String> toStr = new Function1<Object, String>() {
            @Override
            public String apply(Object arg) {
                return arg.toString();
            }
        };

        Iterable<String> mapped = Collections.map(toStr, Arrays.asList(1, 2, 3, 4));
        assertEquals(mapped, Arrays.asList("1", "2", "3", "4"));
    }

    @Test
    public void testFilter() {
        Predicate<Integer> mod2 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 2 == 0;
            }
        };

        Iterable<Integer> filtered = Collections.filter(mod2, Arrays.asList(1, 2, 3, 4));
        assertEquals(filtered, Arrays.asList(2, 4));
    }

    @Test
    public void testTakeWhile() {
        Predicate<Integer> mod3 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 3 != 0;
            }
        };

        Iterable<Integer> taken = Collections.takeWhile(mod3, Arrays.asList(1, 2, 3, 4));
        assertEquals(taken, Arrays.asList(1, 2));

        assertEquals(Collections.takeWhile(Predicate.ALWAYS_TRUE,
                new ArrayList<Integer>()), new ArrayList<Integer>());
    }

    @Test
    public void testTakeUnless() {
        Predicate<Integer> mod3 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 3 == 0;
            }
        };

        Iterable<Integer> taken = Collections.takeUnless(mod3, Arrays.asList(1, 2, 3, 4));
        assertEquals(taken, Arrays.asList(1, 2));
    }

    @Test
    public void testFoldr() {
        Function2<Integer, String, String> f = new Function2<Integer, String, String>() {
            @Override
            public String apply(Integer arg1, String arg2) {
                return arg1 + "-" + arg2;
            }
        };
        String res = Collections.foldr(f, "$", Arrays.asList(1, 2, 3));
        assertEquals(res, "1-2-3-$");
    }

    @Test
    public void testFoldl() {
        Function2<String, Integer, String> f = new Function2<String, Integer, String>() {
            @Override
            public String apply(String arg1, Integer arg2) {
                return arg1 + "-" + arg2;
            }
        };

        String res = Collections.foldl(f, "$", Arrays.asList(1, 2, 3));
        assertEquals(res, "$-1-2-3");
    }
}
