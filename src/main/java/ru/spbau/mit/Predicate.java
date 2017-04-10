package ru.spbau.mit;

public abstract class Predicate<A> extends Function1<A, Boolean> {
    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object arg) {
            return true;
        }
    };

    public static final Predicate<Object> ALWAYS_FALSE = ALWAYS_TRUE.not();

    Predicate<A> or(final Predicate<? super A> p) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return Predicate.this.apply(arg) || p.apply(arg);
            }
        };
    }

    Predicate<A> and(final Predicate<? super A> p) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return Predicate.this.apply(arg) && p.apply(arg);
            }
        };
    }

    Predicate<A> not() {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A arg) {
                return !Predicate.this.apply(arg);
            }
        };
    }


}
