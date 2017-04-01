package ru.spbau.mit;

abstract class Function1<T, R> {

    public abstract R apply(T t);

    public <R2>
    Function1<T, R2> compose(final Function1<? super R, R2> g) {
        return new Function1<T, R2>() {
            @Override
            public R2 apply(T t) {
                return g.apply(Function1.this.apply(t));
            }
        };
    }
}
