package ru.spbau.mit;

public abstract class Predicate<A> extends Function1<A, Boolean> {
    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object argument) {
            return Boolean.TRUE;
        }
    };
    public static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object argument) {
            return Boolean.FALSE;
        }
    };

    public abstract Boolean apply(A argument);

    public Predicate<A> or(final Predicate<? super A> predicate) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A argument) {
                if (Predicate.this.apply(argument)) {
                    return Boolean.TRUE;
                } else {
                    return predicate.apply(argument);
                }
            }
        };
    }

    public Predicate<A> and(final Predicate<? super A> predicate) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A argument) {
                if (!Predicate.this.apply(argument)) {
                    return Boolean.FALSE;
                } else {
                    return predicate.apply(argument);
                }
            }
        };
    }

    public Predicate<A> not() {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A argument) {
                return !Predicate.this.apply(argument);
            }
        };
    }
}
