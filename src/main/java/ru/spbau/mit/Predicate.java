package ru.spbau.mit;

public abstract class Predicate<T> extends Function1<Boolean, T> {
    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        public Boolean apply(Object param) {
            return true;
        }
    };
    public static final Predicate<Object> ALWAYS_FALSE = ALWAYS_TRUE.not();

    public Predicate<T> or(final Predicate<? super T> predicate) {
        return new Predicate<T>() {
            public Boolean apply(T param) {
                return Predicate.this.apply(param) || predicate.apply(param);
            }
        };
    }

    public Predicate<T> and(final Predicate<? super T> predicate) {
        return new Predicate<T>() {
            public Boolean apply(T param) {
                return Predicate.this.apply(param) && predicate.apply(param);
            }
        };
    }

    public Predicate<T> not() {
        return new Predicate<T>() {
            public Boolean apply(T param) {
                return !Predicate.this.apply(param);
            }
        };
    }
}
