package ru.spbau.mit;

public final class TestUtils {

    private TestUtils() {}

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

    static Function1<Integer, Integer> id() {
        return new Function1<Integer, Integer>() {
            @Override
            Integer apply(Integer integer) {
                return integer;
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
