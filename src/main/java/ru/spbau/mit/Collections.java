package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Collections {
    private Collections() {
    }

    @NotNull
    public static <T, R> Iterable<R> map(@NotNull final Function1<? super T, ? extends R> function,
                                         @NotNull final Iterable<? extends T> iter) {
        final List<R> buffer = new ArrayList<>();
        for (T item : iter) {
            buffer.add(function.apply(item));
        }

        return buffer;
    }

    @NotNull
    public static <T> Iterable<T> filter(@NotNull final Predicate<? super T> predicate,
                                         @NotNull final Iterable<? extends T> iter) {
        final List<T> buffer = new ArrayList<>();
        for (T item : iter) {
            if (predicate.apply(item)) {
                buffer.add(item);
            }
        }

        return buffer;
    }

    @NotNull
    public static <T> Iterable<T> takeWhile(@NotNull final Predicate<? super T> predicate,
                                            @NotNull final Iterable<? extends T> iter) {
        final List<T> buffer = new ArrayList<>();
        for (T item : iter) {
            if (!predicate.apply(item)) {
                break;
            }

            buffer.add(item);
        }

        return buffer;
    }

    @NotNull
    public static <T> Iterable<T> takeUnless(@NotNull final Predicate<? super T> predicate,
                                             @NotNull final Iterable<? extends T> iter) {
        return takeWhile(predicate.not(), iter);
    }

    @Nullable
    public static <T, I> I foldl(@NotNull final Function2<? super I, ? super T, ? extends I> combiner,
                                 @Nullable final I accumulator,
                                 @NotNull final Iterable<? extends T> iter) {
        return foldl(combiner, accumulator, iter.iterator());
    }

    @Nullable
    public static <T, I> I foldr(@NotNull final Function2<T, I, I> combiner,
                                 @Nullable final I initial,
                                 @NotNull final Iterable<T> iter) {
        return foldr(combiner, initial, iter.iterator());
    }

    @Nullable
    private static <T, I> I foldl(@NotNull final Function2<? super I, ? super T, ? extends I> combiner,
                                  @Nullable final I accumulator,
                                  @NotNull final Iterator<? extends T> iter) {
        if (iter.hasNext()) {
            return foldl(combiner, combiner.apply(accumulator, iter.next()), iter);
        }

        return accumulator;
    }

    @Nullable
    private static <T, I> I foldr(@NotNull final Function2<? super T, ? super I, ? extends I> combiner,
                                  @Nullable final I initial,
                                  @NotNull final Iterator<? extends T> iter) {
        if (iter.hasNext()) {
            return combiner.apply(iter.next(), foldr(combiner, initial, iter));
        }

        return initial;
    }
}
