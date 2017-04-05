package ru.spbau.mit;

public abstract class Predicate<X> {

    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        boolean apply(Object x) {
            return true;
        }
    };

    public static final Predicate<Object> ALWAYS_FALSE = ALWAYS_TRUE.not();

    abstract boolean apply(X x);

    Predicate<X> or(final Predicate<? super X> other) {
        return new Predicate<X>() {
            @Override
            boolean apply(X x) {
                return Predicate.this.apply(x)  || other.apply(x);
            }
        };
    }

    Predicate<X> and(final Predicate<? super X> other) {
        return new Predicate<X>() {
            @Override
            boolean apply(X x) {
                return Predicate.this.apply(x) && other.apply(x);
            }
        };
    }

    Predicate<X> not() {
        return new Predicate<X>() {
            @Override
            boolean apply(X x) {
                return !Predicate.this.apply(x);
            }
        };
    }
}
