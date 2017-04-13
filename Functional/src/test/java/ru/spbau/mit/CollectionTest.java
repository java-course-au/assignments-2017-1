package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionTest {

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


    @Test
    public void WildcardsCollectionMapTest() {
        Collection<Integer> integerCollection = new ArrayList<>();
        int size = 10;
        for (int i = 0; i < size; i++) {
            integerCollection.add(i);
        }

        Function1<Object, Object> f = new Function1<Object, Object>() {
            @Override
            public Object apply(Object o) {
                return o;
            }
        };

        Assert.assertEquals(integerCollection, Collections.map(f, integerCollection));
    }

    @Test
    public void WildcardsCollectionFilterTest() {
        List<Integer> res = Collections.filter(new Predicate<Object>() {
            @Override
            public Boolean apply(Object object) {
                return null;
            }
        }, new ArrayList<Integer>());
    }

    @Test
    public void WildcardsCollectionWhileTest() {
        List<Integer> res = Collections.takeWhile(new Predicate<Object>() {
            @Override
            public Boolean apply(Object object) {
                return null;
            }
        }, new ArrayList<Integer>());
    }

    @Test
    public void WildcardsCollectionUnlessTest() {
        List<Integer> res = Collections.takeUnless(new Predicate<Object>() {
            @Override
            public Boolean apply(Object object) {
                return null;
            }
        }, new ArrayList<Integer>());
    }

    @Test
    public void WildcardCillectionFoldTest() {
        Integer res = Collections.foldl(new Function2<Object, Object, Integer>() {
            @Override
            public Integer apply(Object o, Object o2) {
                return null;
            }
        }, new Integer(1), new ArrayList<Integer>());

        res = Collections.foldr(new Function2<Object, Object, Integer>() {

            @Override
            public Integer apply(Object o, Object o2) {
                return null;
            }
        }, new Integer(1), new ArrayList<Integer>());
    }

}
