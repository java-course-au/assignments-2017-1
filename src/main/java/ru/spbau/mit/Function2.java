package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Function2<A, B, C> {
    @Nullable
    abstract C apply(@Nullable final A arg1, @Nullable final B arg2);

    @NotNull
    public <D> Function2<A, B, D> compose(@NotNull final Function1<? super C, ? extends D> other) {
        return new Function2<A, B, D>() {
            @Nullable
            @Override
            D apply(@Nullable final A arg1, @Nullable final B arg2) {
                return other.apply(Function2.this.apply(arg1, arg2));
            }
        };
    }

    @NotNull
    public Function1<B, C> bind1(@Nullable final A arg1) {
        return new Function1<B, C>() {
            @Nullable
            @Override
            C apply(@Nullable final B arg2) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    @NotNull
    public Function1<A, C> bind2(@Nullable final B arg2) {
        return new Function1<A, C>() {
            @Nullable
            @Override
            C apply(@Nullable final A arg1) {
                return Function2.this.apply(arg1, arg2);
            }
        };
    }

    @NotNull
    public Function1<A, Function1<B, C>> curry() {
        return new Function1<A, Function1<B, C>>() {
            @NotNull
            @Override
            Function1<B, C> apply(@Nullable final A arg1) {
                return Function2.this.bind1(arg1);
            }
        };
    }
}
