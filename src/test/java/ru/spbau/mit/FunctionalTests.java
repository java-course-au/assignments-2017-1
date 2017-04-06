package ru.spbau.mit;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionalTests {
    @Test
    public void smokeTest() {

        Function1<Integer, Integer> multiplyByTwo = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * 2;
            }
        };

        assertEquals(20, multiplyByTwo.apply(10).intValue());

        Function2<Integer, Integer, Integer> plus = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };

        assertEquals(30, plus.apply(10, 20).intValue());

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 1;
            }
        };

        assertTrue(isEven.test(33));
    }

    @Test
    public void composeTest() {
        Function1<Integer, Integer> multiplyByTwo = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * 2;
            }
        };

        Function1<Integer, Integer> addThree = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer + 3;
            }
        };

        assertEquals(7, multiplyByTwo.compose(addThree).apply(2).intValue());

        Function1<Integer, Integer> negate = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return -integer;
            }
        };

        Function2<Integer, Integer, Integer> multiply = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer * integer2;
            }
        };

        assertEquals(-300, multiply.compose(negate).apply(150, 2).intValue());
    }

    @Test
    public void bindCurryTest() {
        Function2<Integer, Integer, Integer> power = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return (int) Math.pow(integer, integer2);
            }
        };

        assertEquals(8, power.apply(2, 3).intValue());
        Function1<Integer, Integer> powerOfTwo = power.bind1(2);

        assertEquals(1024, powerOfTwo.apply(10).intValue());

        Function1<Integer, Integer> square = power.bind2(2);

        assertEquals(625, square.apply(25).intValue());

        assertEquals(625, power.curry().apply(25).apply(2).intValue());
    }

    @Test
    public void predicatesTest() {

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 1;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 0;
            }
        };

        assertTrue(isEven.or(isOdd).test(444));
        assertFalse(isEven.and(isOdd).test(444));
        assertTrue(isEven.not().and(isOdd).test(444));
        assertTrue(Predicate.ALWAYS_TRUE.test(null));
        assertFalse(Predicate.ALWAYS_FALSE.test(null));
    }

    @Test
    public void inheritanceTest() {
        Function1<Object, String> stringifier = new Function1<Object, String>() {
            @Override
            public String apply(Object o) {
                return o.toString();
            }
        };

        Function2<Integer, Integer, Integer> add = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };


        Function1<Integer, Integer> addTwo = add.bind1(2);

        assertEquals("11235", addTwo.compose(stringifier).apply(11233));
        assertEquals("4", add.compose(stringifier).apply(2,2));

    }

    @Test
    public void foldrTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Function2<Integer, Integer, Integer> add = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };
        Integer sumres = Collections.foldr(add, 0, list);
        assertEquals(6, sumres.intValue());

        List<Integer> list2 = new ArrayList<>();

        Function2<Integer, List<Integer>, List<Integer>> reverser = new Function2<Integer, List<Integer>, List<Integer>>() {
            @Override
            public List<Integer> apply(Integer integer, List<Integer> integers) {
                integers.add(integer);
                return integers;
            }
        };

        Collections.foldr(reverser, list2, list);
        java.util.Collections.reverse(list);
        assertTrue(list.equals(list2));
    }

    @Test
    public void foldlTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        List<Integer> same = new ArrayList<>();

        Function2<Integer, List<Integer>, List<Integer>> id = new Function2<Integer, List<Integer>, List<Integer>>() {
            @Override
            public List<Integer> apply(Integer integer, List<Integer> integers) {
                integers.add(integer);
                return integers;
            }
        };

        Collections.foldl(id, same, list);
        assertTrue(list.equals(same));
    }

    @Test
    public void mapTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Iterable<String> mapped = Collections.map(new Function1<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return integer.toString();
            }
        }, list);

        assertEquals(list.stream().map(Object::toString).collect(Collectors.toList()), mapped);
    }

    @Test
    public void filterTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);

        Iterable<Integer> even = Collections.filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 0;
            }
        }, list);

        assertEquals(list.stream().filter(i -> i % 2 == 0).collect(Collectors.toList()), even);

    }

    @Test
    public void takeTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(5);
        list.add(0);
        list.add(0);
        list.add(7);

        Predicate<Integer> lessThanFive = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer < 5;
            }
        };

        Predicate<Integer> moreThanFive = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 5;
            }
        };


        Iterable<Integer> less = Collections.takeWhile(lessThanFive, list);
        Iterable<Integer> more = Collections.takeUnless(moreThanFive, list);

        List<Integer> lessExpected = new ArrayList<>();
        lessExpected.add(1);
        lessExpected.add(2);
        lessExpected.add(3);

        List<Integer> moreExpected = new ArrayList<>();
        moreExpected.add(1);
        moreExpected.add(2);
        moreExpected.add(3);
        moreExpected.add(5);
        moreExpected.add(0);
        moreExpected.add(0);

        assertEquals(lessExpected, less);
        assertEquals(moreExpected, more);
    }
}
