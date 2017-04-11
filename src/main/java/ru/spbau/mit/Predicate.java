package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Predicate<T> extends Function1<T, Boolean> {
    @NotNull
    public static final Predicate<?> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(@Nullable final Object arg) {
            return true;
        }
    };

    @NotNull
    public static final Predicate<?> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public Boolean apply(@Nullable final Object arg) {
            return false;
        }
    };

    @NotNull
    public Predicate<T> or(@NotNull final Predicate<? super T> other) {
        return new Predicate<T>() {
            @NotNull
            @Override
            Boolean apply(@Nullable final T arg) {
                return Predicate.this.apply(arg) || other.apply(arg);
            }
        };
    }

    @NotNull
    public Predicate<T> and(@NotNull final Predicate<? super T> other) {
        return new Predicate<T>() {
            @NotNull
            @Override
            Boolean apply(@Nullable final T arg) {
                return Predicate.this.apply(arg) && other.apply(arg);
            }
        };
    }

    @NotNull
    public Predicate<T> not() {
        return new Predicate<T>() {
            @NotNull
            @Override
            Boolean apply(@Nullable final T arg) {
                return !Predicate.this.apply(arg);
            }
        };
    }
}
