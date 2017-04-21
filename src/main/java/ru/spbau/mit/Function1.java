package ru.spbau.mit;

public abstract class Function1<X, Y> {

    public abstract Y apply(X x);

    public <T> Function1<X, T> compose(final Function1<? super Y, ? extends T> g) {
        return new Function1<X, T>() {
            @Override
            public T apply(X x) {
                return g.apply(Function1.this.apply(x));
            }
        };
    }
}
