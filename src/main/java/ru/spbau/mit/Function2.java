package ru.spbau.mit;

public abstract class Function2<R, X, Y> {
    abstract R apply(X x, Y y);

    <Z> Function2<Z, X, Y> compose(final Function1<Z, ? super R> g) {
        return new Function2<Z, X, Y>() {
            @Override
            Z apply(X x, Y y) {
                return g.apply(Function2.this.apply(x, y));
            }
        };
    }

    Function1<R, Y> bind1(final X x) {
        return new Function1<R, Y>() {
            @Override
            R apply(Y y) {
                return Function2.this.apply(x, y);
            }
        };
    }

    Function1<R, X> bind2(final Y y) {
        return new Function1<R, X>() {
            @Override
            R apply(X x) {
                return Function2.this.apply(x, y);
            }
        };
    }

    Function1<Function1<R, Y>, X> curry() {
        return new Function1<Function1<R, Y>, X>() {
            @Override
            Function1<R, Y> apply(X x) {
                return Function2.this.bind1(x);
            }
        };
    }

}
