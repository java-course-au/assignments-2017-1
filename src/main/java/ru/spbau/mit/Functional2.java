package ru.spbau.mit;

abstract class Functional2<X, Y, Z> {
    abstract Z apply(X x, Y y);

    <R> Functional2<X, Y, R> compose(final Functional1<? super Z, R> g) {
        return new Functional2<X, Y, R>() {
            @Override
            R apply(X x, Y y) {
                return g.apply(Functional2.this.apply(x, y));
            }
        };
    }

    Functional1<Y, Z> bind1(final X x) {
        return new Functional1<Y, Z>() {
            @Override
            Z apply(Y y) {
                return Functional2.this.apply(x, y);
            }
        };
    }

    Functional1<X, Z> bind2(final Y y) {
        return new Functional1<X, Z>() {
            @Override
            Z apply(X x) {
                return Functional2.this.apply(x, y);
            }
        };
    }

    Functional1<X, Functional1<Y, Z>> curry() {
        return new Functional1<X, Functional1<Y, Z>>() {
            @Override
            Functional1<Y, Z> apply(X x) {
                return Functional2.this.bind1(x);
            }
        };
    }
}
