package ru.spbau.mit;

public abstract class Function1<X, Y> {
    abstract Y apply(X x);

    public <S> Function1<X, S> compose(final Function1<? super Y, S> g) {
        return new Function1<X, S>() {
            @Override
            S apply(X x) {
                return g.apply(Function1.this.apply(x));
            }
        };
    }
}
