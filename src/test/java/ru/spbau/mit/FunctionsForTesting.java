package ru.spbau.mit;

import java.util.Iterator;

public final class FunctionsForTesting {
    static final int SIZE = 15;

    private FunctionsForTesting() {}

    static <T> boolean compareCollections(Iterable<T> col1, Iterable<T> col2) {
        Iterator<T> it1 = col1.iterator();
        Iterator<T> it2 = col2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!it1.next().equals(it2.next())) {
                return false;
            }
        }
        return true;
    }

    static Function1<Integer, Integer> succ() {
        return new Function1<Integer, Integer>() {
            @Override
            Integer apply(Integer integer) {
                return integer + 1;
            }
        };
    }

    static Function1<Integer, Integer> mult2() {
        return new Function1<Integer, Integer>() {
            @Override
            Integer apply(Integer integer) {
                return integer * 2;
            }
        };
    }

    static Function2<Integer, Integer, Integer> sum() {
        return new Function2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        };
    }

    static Function2<Integer, Integer, Integer> mult() {
        return new Function2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer * integer2;
            }
        };
    }

    static <X extends Number> Predicate<X> greaterThan(final X number) {
        return new Predicate<X>() {
            @Override
            boolean apply(X x) {
                return x.intValue() > number.intValue();
            }
        };
    }


    static <X extends Number> Predicate<X> lessThan(final X number) {
        return new Predicate<X>() {
            @Override
            boolean apply(X x) {
                return x.intValue() < number.intValue();
            }
        };
    }

    static <T> int size(Iterable<T> col) {
        int i = 0;
        for (T aCol : col) {
            ++i;
        }
        return i;
    }
}
