package ru.spbau.mit;

public abstract class Function1<R, X> {

    abstract R apply(X x);

    <Y> Function1<Y, X> compose(final Function1<Y, ? super R> g) {
        return new Function1<Y, X>() {
            @Override
            Y apply(X x) {
                return g.apply(Function1.this.apply(x));
            }
        };
    }

}
