package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class FunctionalTest {

    @Test
    public void Function1Test() {
        Function1<Integer, Integer> f = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer + 1;
            }
        };

        Function1<Integer, Integer> g = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer - 1;
            }
        };

        int value = 10;
        for (int i = value, n = 0; n < 20; i++, n++) {
            Assert.assertTrue(i + 1 == f.apply(i));
            Assert.assertTrue(i - 1 == g.apply(i));
            Assert.assertTrue(i == f.compose(g).apply(i));
        }
    }


    @Test
    public void Function2ComposeTest() {
        Function2<Integer, Integer, Integer> f = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };

        Function1<Integer, Integer> g = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer - 1;
            }
        };

        final int a = 10;
        final int b = 20;

        Assert.assertTrue(a + b == f.apply(a, b));
        Assert.assertTrue(a + b - 1 == f.compose(g).apply(a, b));
        Assert.assertTrue(a + b - 2 == f.compose(g).compose(g).apply(a, b));
    }

    @Test
    public void Function2BindTest() {
        Function2<Integer, Integer, Integer> f = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };

        int value = 10;

        Assert.assertTrue(value + 1 == f.bind1(value).apply(1));
        Assert.assertTrue(value + 2 == f.bind2(value).apply(2));
    }


    @Test
    public void Function2CurryTest() {
        Function2<String, String, String> f = new Function2<String, String, String>() {
            @Override
            public String apply(String s, String s2) {
                return s + s2;
            }
        };

        Function1<String, Function1<String, String>> g = f.curry();
        String a = "aaaaa";
        String b = "bbbbb";

        Assert.assertEquals(a + b, g.apply(a).apply(b));
    }

    @Test
    public void Function2Test() {
        Function2<String, String, String> f = new Function2<String, String, String>() {
            @Override
            public String apply(String s, String s2) {
                return s + s2;
            }
        };

        Function1<String, String> g = new Function1<String, String>() {
            @Override
            public String apply(String s) {
                return new StringBuffer(s).reverse().toString();
            }
        };

        String a = "aba";
        String b = "bab";

        Assert.assertEquals(a + b, f.bind1(a).apply(b));
        Assert.assertEquals(a + b, f.curry().apply(a).apply(b));
        Assert.assertEquals(b + a, f.compose(g).apply(a, b));
    }

    @Test
    public void PredicateApplyTest() {
        Predicate<Integer> p = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        for (int i = 0; i < 10; i += 2) {
            Assert.assertTrue(p.apply(i));
        }
        for (int i = 1; i < 10; i += 2) {
            Assert.assertFalse(p.apply(i));
        }
    }

    @Test
    public void PredicateTest() {
        Predicate<String> p = new Predicate<String>() {
            @Override
            public Boolean apply(String s) {
                return s.contains("a");
            }
        };

        Predicate<String> p1 = new Predicate<String>() {
            @Override
            public Boolean apply(String s) {
                return s.contains("b");
            }
        };

        Predicate<String> p2 = new Predicate<String>() {
            @Override
            public Boolean apply(String s) {
                return s.contains("c");
            }
        };

        String str = "bab";

        Assert.assertTrue(p.and(p1).apply(str));
        Assert.assertTrue(p.or(p2).apply(str));
        Assert.assertFalse(p.and(p2).apply(str));

        Assert.assertTrue(p.and(Predicate.ALWAYS_TRUE).apply(str));

        Assert.assertFalse(Predicate.ALWAYS_FALSE.apply(str));
        Assert.assertFalse(Predicate.ALWAYS_FALSE.apply(1));
        Assert.assertTrue(Predicate.ALWAYS_TRUE.apply(1));

        Assert.assertFalse(Predicate.ALWAYS_FALSE.and(p).apply(str));
    }

    @Test
    public void CollecionsMapTest() {

        Function1<Integer, Integer> f = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer + 1;
            }
        };

        ArrayList<Integer> value = new ArrayList<>();

        final int size = 10;

        for (int i = 0; i < size; i++) {
            value.add(i);
        }

        ArrayList<Integer> res = new ArrayList<>();
        for (Integer i : value) {
            res.add(i + 1);
        }

        Assert.assertEquals(res, Collections.map(f, value));

        Function1<Integer, Integer> g = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer - 1;
            }
        };

        Assert.assertEquals(value, Collections.map(g, res));
    }

    @Test
    public void CollectionsFilterTest() {
        Predicate<String> p = new Predicate<String>() {
            @Override
            public Boolean apply(String s) {
                return s.contains("a");
            }
        };

        ArrayList<String> str = new ArrayList<>();
        str.add("bbbb");
        str.add("aaa");
        str.add("ccc");

        ArrayList<String> res = new ArrayList<>();
        for (String s : str) {
            if (s.contains("a")) {
                res.add(s);
            }
        }

        Assert.assertEquals(res, Collections.filter(p, str));

        Predicate<String> p1 = new Predicate<String>() {
            @Override
            public Boolean apply(String s) {
                return s.contains("d");
            }
        };
        Assert.assertFalse(Collections.filter(p1, str).iterator().hasNext());
    }

    @Test
    public void CollectionTakeTest() {
        Predicate<Integer> p = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        ArrayList<Integer> value = new ArrayList<>();
        for (int i = 0; i < 100; i += 2) {
            value.add(i);
        }
        Assert.assertEquals(value, Collections.takeWhile(p, value));
        Assert.assertFalse(Collections.takeWhile(p.not(), value).iterator().hasNext());

        Assert.assertFalse(Collections.takeUnless(p, value).iterator().hasNext());
        Assert.assertEquals(value, Collections.takeUnless(p.not(), value));
    }

    @Test
    public void CollectionsFolderTest() {
        Function2<Integer, Integer, Integer> f = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };

        ArrayList<Integer> value = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            value.add(i);
        }
        Assert.assertEquals(Collections.foldl(f, 0, value), Collections.foldr(f, 0, value));
    }
}
