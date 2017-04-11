package ru.spbau.mit;

public abstract class Function1<A1, R> {

    public abstract R apply(A1 argument);

    public <Y> Function1<A1, Y> compose(final Function1<? super R, Y> g) {
        return new Function1<A1, Y>() {
            @Override
            public Y apply(A1 argument) {
                return g.apply(Function1.this.apply(argument));
            }
        };
    }
}
