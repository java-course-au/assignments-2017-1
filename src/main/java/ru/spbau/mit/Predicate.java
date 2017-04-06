package ru.spbau.mit;

public abstract class Predicate<X> extends Function1<X, Boolean> {
    public static final Predicate<Object> ALWAYS_TRUE =
            new Predicate<Object>() {
                @Override
                Boolean apply(Object o) {
                    return Boolean.TRUE;
                }
            };

    public static final Predicate<Object> ALWAYS_FALSE =
            new Predicate<Object>() {
                @Override
                Boolean apply(Object o) {
                    return Boolean.FALSE;
                }
            };

    abstract Boolean apply(X x);

    public Predicate<X> or(final Predicate<? super X> predicate) {
        return new Predicate<X>() {
            @Override
            Boolean apply(X x) {
                return Predicate.this.apply(x) || predicate.apply(x);
            }
        };
    }

    public Predicate<X> and(final Predicate<? super X> predicate) {
        return new Predicate<X>() {
            @Override
            Boolean apply(X x) {
                return Predicate.this.apply(x) && predicate.apply(x);
            }
        };
    }

    public Predicate<X> not() {
        return new Predicate<X>() {
            @Override
            Boolean apply(X x) {
                return !Predicate.this.apply(x);
            }
        };
    }
}
