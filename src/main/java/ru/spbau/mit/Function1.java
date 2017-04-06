package ru.spbau.mit;

public abstract class Function1<R, T> {
    public abstract R apply(T param);

    public <S> Function1<S, T> compose(final Function1<S, ? super R> g) {
        return new Function1<S, T>() {
            public S apply(T param) {
                 return g.apply(Function1.this.apply(param));
            }
        };
    }
}
