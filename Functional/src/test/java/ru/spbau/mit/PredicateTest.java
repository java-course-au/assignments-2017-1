package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import static ru.spbau.mit.Wildcards.Base;
import static ru.spbau.mit.Wildcards.Derived;

public class PredicateTest {

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

        Assert.assertFalse(p.not().apply(str));
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
    public void PredicateLazy() {
        final Predicate<Boolean> TRUE = new Predicate<Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean) {
                return aBoolean != null && aBoolean;
            }
        };

        final Predicate<Boolean> FALSE = new Predicate<Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean) {
                return aBoolean != null && !aBoolean;
            }
        };

        final Predicate<Boolean> THROW = new Predicate<Boolean>() {
            @Override
            public Boolean apply(Boolean arg) {
                throw new IllegalStateException();
            }
        };


        Assert.assertTrue(TRUE.or(THROW).apply(true));
        Assert.assertFalse(FALSE.and(THROW).apply(true));


        Assert.assertFalse(TRUE.not().and(THROW).apply(true));
        Assert.assertTrue(FALSE.not().or(THROW).apply(true));

    }

    @Test
    public void WildcardsPredicateTest() {
        Predicate<Derived> p1 = new Predicate<Derived>() {
            @Override
            public Boolean apply(Derived derived) {
                return null;
            }
        };

        Predicate<Base> p2 = new Predicate<Base>() {
            @Override
            public Boolean apply(Base base) {
                return null;
            }
        };

        Predicate<Derived> resOr = p1.or(p2);
        Predicate<Derived> resAnd = p1.and(p2);
    }
}
