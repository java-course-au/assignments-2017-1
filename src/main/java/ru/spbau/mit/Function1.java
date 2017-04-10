package ru.spbau.mit;

public abstract class Function1<A, R> {
    public abstract R apply(A arg);

    public <RG> Function1<A, RG> compose(final Function1<? super R, RG> g) {
        return new Function1<A, RG>() {
            @Override
            public RG apply(A arg) {
                return g.apply(Function1.this.apply(arg));
            }
        };
    }
}
