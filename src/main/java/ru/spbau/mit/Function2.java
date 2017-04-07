package ru.spbau.mit;

public abstract class Function2<A1, A2, R> {
    public abstract R apply(A1 argument1, A2 argument2);

    public <Y> Function2<A1, A2, Y> compose(final Function1<? super R, Y> g) {
        return new Function2<A1, A2, Y>() {
            @Override
            public Y apply(A1 argument1, A2 argument2) {
                return g.apply(Function2.this.apply(argument1, argument2));
            }
        };
    }

    public Function1<A2, R> bind1(final A1 argument1) {
        return new Function1<A2, R>() {
            @Override
            public R apply(A2 argument2) {
                return Function2.this.apply(argument1, argument2);
            }
        };
    }

    public Function1<A1, R> bind2(final A2 argument2) {
        return new Function1<A1, R>() {
            @Override
            public R apply(A1 argument1) {
                return Function2.this.apply(argument1, argument2);
            }
        };
    }

    public Function1<A1, Function1<A2, R>> curry() {
        return new Function1<A1, Function1<A2, R>>() {
            @Override
            public Function1<A2, R> apply(A1 argument) {
                return bind1(argument);
            }
        };
    }
}
