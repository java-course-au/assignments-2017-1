package ru.spbau.mit;

public abstract class Function1<X, R> {

    abstract R apply(X x);

    <R1> Function1<X, R1> compose(final Function1<? super R, R1> g) {
        return new Function1<X, R1>() {
            @Override
            R1 apply(X x) {
                return g.apply(Function1.this.apply(x));
            }
        };
    }

}
