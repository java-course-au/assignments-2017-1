package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class Functional2Test {
    @Test
    public void apply() throws Exception {
        Functional2<String, String, Integer> v = new Functional2<String, String, Integer>() {
            @Override
            Integer apply(String s, String s2) {
                return s.length() + s2.length();
            }
        };
        final Integer check = 10;
        assertEquals(check, v.apply("Test", "String"));
    }

    @Test
    public void compose() throws Exception {
        Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer * integer2;
            }
        };
        Functional1<Integer, Integer> v = new Functional1<Integer, Integer>() {
          @Override
          Integer apply(Integer integer) {
              final int c = 2;
              return integer * c;
          }
        };
        final Integer check = 100;
        final Integer param1 = 25;
        final Integer param2 = 2;
        assertEquals(u.compose(v).apply(param1, param2), check);
    }

    @Test
    public void bind1() throws Exception {
        Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };
        final int param1 = 20;
        Functional1<Integer, Integer> v = u.bind1(param1);
        final Integer check = 50;
        final int param2 = 30;
        assertEquals(v.apply(param2), check);
    }

    @Test
    public void bind2() throws Exception {
        Functional2<Integer, Integer, Integer> u = new Functional2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer - integer2;
            }
        };
        final int param2 = 20;
        Functional1<Integer, Integer> v = u.bind2(param2);
        final Integer check = 10;
        final int param1 = 30;
        assertEquals(v.apply(param1), check);
    }

    @Test
    public void curry() throws Exception {
        Functional2<Integer, ArrayList<Integer>, Integer> u =
                new Functional2<Integer, ArrayList<Integer>, Integer>() {
            @Override
            Integer apply(Integer integer, ArrayList<Integer> integers) {
                Integer sum = 0;
                for (Integer i : integers) {
                    sum += i * integer;
                }
                return sum;
            }
        };
        Functional1<Integer, Functional1<ArrayList<Integer>, Integer>> curry = u.curry();
        final int param1 = 10;
        final Functional1<ArrayList<Integer>, Integer> func = curry.apply(param1);
        ArrayList<Integer> in = new ArrayList<>();
        final int n = 5;
        for (int i = 1; i < n; i++) {
            in.add(i);
        }
        final Integer check = 100;
        assertEquals(func.apply(in), check);
    }
}
