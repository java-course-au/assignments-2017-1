package ru.spbau.mit;

public abstract class Function2<X1, X2, R> {
    abstract R apply(X1 x1, X2 x2);

    public <S> Function2<X1, X2, S> compose(final Function1<? super R, S> g) {
        return new Function2<X1, X2, S>() {
            @Override
            S apply(X1 x1, X2 x2) {
                return g.apply(Function2.this.apply(x1, x2));
            }
        };
    }

    public Function1<X2, R> bind1(final X1 x1) {
        return new Function1<X2, R>() {
            @Override
            R apply(X2 x2) {
                return Function2.this.apply(x1, x2);
            }
        };
    }

    public Function1<X1, R> bind2(final X2 x2) {
        return new Function1<X1, R>() {
            @Override
            R apply(X1 x1) {
                return Function2.this.apply(x1, x2);
            }
        };
    }

    public Function1<X1, Function1<X2, R>> curry() {
        return new Function1<X1, Function1<X2, R>>() {
            @Override
            Function1<X2, R> apply(X1 x1) {
                return Function2.this.bind1(x1);
            }
        };
    }
}
