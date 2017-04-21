package ru.spbau.mit;

public abstract class Predicate<U> {

    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public boolean test(Object objects) {
            return true;
        }
    };

    public static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public boolean test(Object objects) {
            return false;
        }
    };

    public abstract boolean test(U u);

    public Predicate<U> or(final Predicate<U> other) {
        return new Predicate<U>() {
            @Override
            public boolean test(U u) {
                return Predicate.this.test(u) || other.test(u);
            }
        };
    }

    public Predicate<U> and(final Predicate<U> other) {
        return new Predicate<U>() {
            @Override
            public boolean test(U u) {
                return Predicate.this.test(u) && other.test(u);
            }
        };
    }

    public Predicate<U> not() {
        return new Predicate<U>() {
            @Override
            public boolean test(U u) {
                return !Predicate.this.test(u);
            }
        };
    }
}
