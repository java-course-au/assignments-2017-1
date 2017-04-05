package ru.spbau.mit;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CollectionsTest {
    @Test
    public void map() throws Exception {
        Functional1<Integer, Integer> pow = new Functional1<Integer, Integer>() {
            @Override
            Integer apply(Integer integer) {
                return integer * integer;
            }
        };
        ArrayList<Integer> list = new ArrayList<>();
        final int n = 10;
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        Iterable<Integer> result = Collections.map(pow, list);
        Iterator<Integer> it = result.iterator();
        for (int i = 0; i < n; i++) {
            int check = it.next();
            assertEquals(check, i * i);
        }
    }

    @Test
    public void filter() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                final int d = 2;
                return integer % d == 0;
            }
        };
        ArrayList<Integer> list = new ArrayList<>();
        final int n = 10;
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        Iterable<Integer> result = Collections.filter(f, list);
        Iterator<Integer> it = result.iterator();
        for (int i = 0; i < n; i = i + 2) {
            int check = it.next();
            assertEquals(check, i);
        }
    }

    @Test
    public void takeWhile() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                final int d = 5;
                return integer < d;
            }
        };
        ArrayList<Integer> list = new ArrayList<>();
        final int n = 10;
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        Iterable<Integer> result = Collections.takeWhile(f, list);
        Iterator<Integer> it = result.iterator();
        ArrayList<Integer> check = new ArrayList<>();
        for (int i = 0; i < n / 2; i++) {
            check.add(i);
        }
        int i = 0;
        while (it.hasNext()) {
            int val = it.next();
            int ch = check.get(i);
            assertEquals(ch, val);
            i++;
        }
    }

    @Test
    public void takeWhileNull() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                final int d = 0;
                return integer < d;
            }
        };
        ArrayList<Integer> list = new ArrayList<>();
        final int n = 10;
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        Iterable<Integer> result = Collections.takeWhile(f, list);
        assertTrue(!result.iterator().hasNext());
    }

    @Test
    public void takeUnless() throws Exception {
        Predicate<Integer> f = new Predicate<Integer>() {
            @Override
            Boolean apply(Integer integer) {
                final int d = 5;
                return integer <= d;
            }
        };
        ArrayList<Integer> list = new ArrayList<>();
        final int n = 10;
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        Iterable<Integer> result = Collections.takeUnless(f, list);
        Iterator<Integer> it = result.iterator();
        ArrayList<Integer> check = new ArrayList<>();
        for (int i = 0; i < n / 2; i++) {
            check.add(i);
        }
        int index = 0;
        while (it.hasNext()) {
            int val = it.next();
            int ch = check.get(index);
            index++;
            assertEquals(ch, val);
        }
    }

    @Test
    public void foldl() throws Exception {
        Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };
        ArrayList<Integer> in = new ArrayList<>();
        final int n = 10;
        for (int i = 0; i < n; i++) {
            in.add(i);
        }
        Integer out = Collections.foldl(u, 0, in);
        final Integer check = 45;
        assertEquals(out, check);
    }

    @Test
    public void foldr() throws Exception {
        Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };
        ArrayList<Integer> in = new ArrayList<>();
        final int n = 10;
        for (int i = 0; i < n; i++) {
            in.add(i);
        }
        Integer out = Collections.foldr(u, 0, in);
        final Integer check = 45;
        assertEquals(out, check);
    }
}
