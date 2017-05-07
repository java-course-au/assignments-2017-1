package ru.spbau.mit;


import org.junit.Assert;
import org.junit.Test;

import static ru.spbau.mit.Wildcards.*;

public class Function1Test {

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
            Assert.assertEquals(new Integer(i), f.compose(g).apply(i));
        }
    }

    @Test
    public void WildcardFunction1Test() {
        Function1<Base, DerivedDerived> f1 = new Function1<Base, DerivedDerived>() {
            @Override
            public DerivedDerived apply(Base base) {
                return null;
            }
        };

        Function1<Base, Derived> f2 = new Function1<Base, Derived>() {
            @Override
            public Derived apply(Base base) {
                return null;
            }
        };

        Derived res = f1.compose(f2).apply(new Base());
    }

}
