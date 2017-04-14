package ru.spbau.mit;

public abstract class Function2<X, Y, R> {
    abstract R apply(X x, Y y);

    <R1> Function2<X, Y, R1> compose(final Function1<? super R, R1> g) {
        return new Function2<X, Y, R1>() {
            @Override
            R1 apply(X x, Y y) {
                return g.apply(Function2.this.apply(x, y));
            }
        };
    }

    Function1<Y, R> bind1(final X x) {
        return new Function1<Y, R>() {
            @Override
            R apply(Y y) {
                return Function2.this.apply(x, y);
            }
        };
    }

    Function1<X, R> bind2(final Y y) {
        return new Function1<X, R>() {
            @Override
            R apply(X x) {
                return Function2.this.apply(x, y);
            }
        };
    }

    Function1<X, Function1<Y, R>> curry() {
        return new Function1<X, Function1<Y, R>>() {
            @Override
            Function1<Y, R> apply(X x) {
                return Function2.this.bind1(x);
            }
        };
    }

}
