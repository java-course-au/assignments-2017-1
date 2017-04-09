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

        final int a = 20;
        final int b = 10;

        assertEquals(a, multiplyByTwo.apply(b).intValue());

        Function2<Integer, Integer, Integer> plus = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };

        final int c = 30;
        final int d = 10;
        final int e = 20;
        assertEquals(c, plus.apply(d, e).intValue());

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 1;
            }
        };

        final int f = 33;
        assertTrue(isEven.test(f));
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
                return integer + 2 + 1;
            }
        };
        final int s = 7;

        assertEquals(s, multiplyByTwo.compose(addThree).apply(2).intValue());

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

        final int a = -300;
        final int b = 150;
        assertEquals(a, multiply.compose(negate).apply(b, 2).intValue());
    }

    @Test
    public void bindCurryTest() {
        Function2<Integer, Integer, Integer> power = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return (int) Math.pow(integer, integer2);
            }
        };

        final int a = 8;
        final int b = 3;

        assertEquals(a, power.apply(2, b).intValue());
        Function1<Integer, Integer> powerOfTwo = power.bind1(2);

        final int c = 1024;
        final int d = 10;
        assertEquals(c, powerOfTwo.apply(d).intValue());

        Function1<Integer, Integer> square = power.bind2(2);

        final int e = 625;
        final int f = 25;
        assertEquals(e, square.apply(f).intValue());

        assertEquals(e, power.curry().apply(f).apply(2).intValue());
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

        final int a = 444;
        assertTrue(isEven.or(isOdd).test(a));
        assertFalse(isEven.and(isOdd).test(a));
        assertTrue(isEven.not().and(isOdd).test(a));
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

        final int a = 11233;
        assertEquals("11235", addTwo.compose(stringifier).apply(a));
        assertEquals("4", add.compose(stringifier).apply(2, 2));

    }

    @Test
    public void foldrTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(2 + 1);
        Function2<Integer, Integer, Integer> add = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };
        Integer sumres = Collections.foldr(add, 0, list);
        final int a = 6;
        assertEquals(a, sumres.intValue());

        List<Integer> list2 = new ArrayList<>();

        Function2<Integer, List<Integer>, List<Integer>> reverser =
                new Function2<Integer, List<Integer>, List<Integer>>() {
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
        final int a = 3;
        final int b = 4;
        final int c = 5;
        list.add(a);
        list.add(b);
        list.add(c);
        List<Integer> same = new ArrayList<>();

        Function2<Integer, List<Integer>, List<Integer>> id =
                new Function2<Integer, List<Integer>, List<Integer>>() {
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
        final int a = 3;
        final int b = 4;
        final int c = 5;
        list.add(a);
        list.add(b);
        list.add(c);

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
        final int a = 3;
        final int b = 4;
        final int c = 5;
        final int d = 6;
        final int e = 7;
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);

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
        final int a = 3;
        final int b = 5;
        final int c = 7;
        list.add(a);
        list.add(b);
        list.add(0);
        list.add(0);
        list.add(c);

        List<Integer> lessExpected = new ArrayList<>();
        lessExpected.add(1);
        lessExpected.add(2);
        lessExpected.add(a);

        List<Integer> moreExpected = new ArrayList<>();
        moreExpected.add(1);
        moreExpected.add(2);
        moreExpected.add(a);
        moreExpected.add(b);
        moreExpected.add(0);
        moreExpected.add(0);

        Predicate<Integer> lessThanFive = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer < 2 + 2 + 1;
            }
        };
        Predicate<Integer> moreThanFive = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 2 + 2 + 1;
            }
        };
        Iterable<Integer> less = Collections.takeWhile(lessThanFive, list);
        Iterable<Integer> more = Collections.takeUnless(moreThanFive, list);

        assertEquals(lessExpected, less);
        assertEquals(moreExpected, more);
    }
}
