package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Function1<A, B> {
    abstract B apply(@Nullable final A arg);

    @NotNull
    public <C> Function1<A, C> compose(@NotNull final Function1<? super B, ? extends C> other) {
        return new Function1<A, C>() {
            @Nullable
            @Override
            C apply(@Nullable final A arg) {
                return other.apply(Function1.this.apply(arg));
            }
        };
    }
}
