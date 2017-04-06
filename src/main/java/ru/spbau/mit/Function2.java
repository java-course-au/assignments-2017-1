package ru.spbau.mit;

public abstract class Function2<X, Y, Z> {
    public abstract Z apply(X x, Y y);

    public <U> Function2<X, Y, U> compose(final Function1<? super Z, ? extends U> g) {
        return new Function2<X, Y, U>() {
            @Override
            public U apply(X x, Y y) {
                return g.apply(Function2.this.apply(x, y));
            }
        };
    }

    public Function1<Y, Z> bind1(final X x) {
        return new Function1<Y, Z>() {
            @Override
            public Z apply(Y y) {
                return Function2.this.apply(x, y);
            }
        };
    }

    public Function1<X, Z> bind2(final Y y) {
        return new Function1<X, Z>() {
            @Override
            public Z apply(X x) {
                return Function2.this.apply(x, y);
            }
        };
    }

    public Function1<X, Function1<Y, Z>> curry() {
        return new Function1<X, Function1<Y, Z>>() {
            @Override
            public Function1<Y, Z> apply(X x) {
                return Function2.this.bind1(x);
            }
        };
    }
}
