package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Functional2Test {
    @Test
    public void testApply() throws Exception {
        Functional2<String, String, Integer> v = new Functional2<String, String, Integer>() {
            @Override
            Integer apply(String s, String s2) {
                return s.length() + s2.length();
            }
        };
        assertEquals(Integer.valueOf(10), v.apply("Test", "String"));
    }

    @Test
    public void testCompose() throws Exception {
        Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer * integer2;
            }
        };
        Functional1<Integer, Integer> v = new Functional1<Integer, Integer>() {
          @Override
          Integer apply(Integer integer) {
              return integer * 2;
          }
        };
        assertEquals(u.compose(v).apply(25, 2), Integer.valueOf(100));
    }

    @Test
    public void testBind1() throws Exception {
        Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };
        Functional1<Integer, Integer> v = u.bind1(20);
        assertEquals(v.apply(30), Integer.valueOf(50));
    }

    @Test
    public void testBind2() throws Exception {
        Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer - integer2;
            }
        };
        Functional1<Integer, Integer> v = u.bind2(20);
        assertEquals(v.apply(30), Integer.valueOf(10));
    }

    @Test
    public void testCurry() throws Exception {
        Functional2<Integer, List<Integer>, Integer> u =
                new Functional2<Integer, List<Integer>, Integer>() {
            @Override
            Integer apply(Integer integer, List<Integer> integers) {
                Integer sum = 0;
                for (Integer i : integers) {
                    sum += i * integer;
                }
                return sum;
            }
        };
        Functional1<Integer, Functional1<List<Integer>, Integer>> curry = u.curry();
        final Functional1<List<Integer>, Integer> func = curry.apply(10);
        final Integer[] in = new Integer[] {1, 2, 3, 4};
        assertEquals(func.apply(Arrays.asList(in)), Integer.valueOf(100));
    }
}
