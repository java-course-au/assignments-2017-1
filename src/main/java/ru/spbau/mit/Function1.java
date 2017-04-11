package ru.spbau.mit;

public abstract class Function1<T, R> {
    public abstract R apply(T param);

    public <S> Function1<T, S> compose(final Function1<? super R, S> g) {
        return new Function1<T, S>() {
            public S apply(T param) {
                 return g.apply(Function1.this.apply(param));
            }
        };
    }
}
