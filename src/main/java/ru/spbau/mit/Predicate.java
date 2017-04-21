package ru.spbau.mit;

abstract class Predicate<X> extends Functional1<X, Boolean> {
    static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        Boolean apply(Object o) {
            return true;
        }
    };
    static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        Boolean apply(Object o) {
            return false;
        }
    };

    Predicate<X> not() {
        return new Predicate<X>() {
            @Override
            Boolean apply(X x) {
                return !Predicate.this.apply(x);
            }
        };
    }

    Predicate<X> or(final Predicate<? super X> g) {
        return new Predicate<X>() {
            @Override
            Boolean apply(X x) {
                return Predicate.this.apply(x) || g.apply(x);
            }
        };
    }

    Predicate<X> and(final Predicate<? super X> g) {
        return new Predicate<X>() {
            @Override
            Boolean apply(X x) {
                return Predicate.this.apply(x) && g.apply(x);
            }
        };
    }
}
