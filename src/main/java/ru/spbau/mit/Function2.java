package ru.spbau.mit;

public abstract class Function2<A1, A2, R> {
    public abstract R apply(A1 arg1, A2 arg2);

    public <RG> Function2<A1, A2, RG> compose(final Function1<? super R, RG> g) {
        return new Function2<A1, A2, RG>() {
            @Override
            public RG apply(A1 arg1, A2 arg2) {
                return g.apply(Function2.this.apply(arg1, arg2));
            }
        };
    }

    public Function1<A2, R> bind1(final A1 arg1) {
        return new Function1<A2, R>() {
            @Override
            public R apply(A2 arg2) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    public Function1<A1, R> bind2(final A2 arg2) {
        return new Function1<A1, R>() {
            @Override
            public R apply(A1 arg1) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    public Function1<A1, Function1<A2, R>> curry() {
        return new Function1<A1, Function1<A2, R>>() {
            @Override
            public Function1<A2, R> apply(A1 arg1) {
                return Function2.this.bind1(arg1);
            }
        };
    }

    public Function2<A2, A1, R> flip() {
        return new Function2<A2, A1, R>() {
            @Override
            public R apply(A2 arg1, A1 arg2) {
                return Function2.this.apply(arg2, arg1);
            }
        };
    }
}
