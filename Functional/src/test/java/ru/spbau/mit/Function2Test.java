package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import static ru.spbau.mit.Wildcards.*;

public class Function2Test {

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
    public void WildcardsFunction2Test() {
        Function2<Base, DerivedDerived, Derived> f1 = new Function2<Base, DerivedDerived, Derived>() {
            @Override
            public Derived apply(Base base, DerivedDerived derivedDerived) {
                return null;
            }
        };

        Function1<Base, Derived> f2 = new Function1<Base, Derived>() {
            @Override
            public Derived apply(Base base) {
                return null;
            }
        };


        Derived res = f1.compose(f2).apply(new Base(), new DerivedDerived());
    }
}
