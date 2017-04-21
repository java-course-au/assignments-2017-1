package ru.spbau.mit;

abstract class Functional1<X, Y> {
    abstract Y apply(X x);

    <Z> Functional1<X, Z> compose(final Functional1<? super Y, Z> g) {
        return new Functional1<X, Z>() {
            @Override
            Z apply(X x) {
                return g.apply(Functional1.this.apply(x));
            }
        };
    }
}
