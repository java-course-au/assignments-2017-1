package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Collections {
    private Collections() {}

    public static <R, T> Iterable<R> map(Function1<? super T, R> f, Iterable<? extends T> a) {
        final List<R> processedElements = new ArrayList<R>();
        for (T item : a) {
            processedElements.add(f.apply(item));
        }
        return processedElements;
    }

    public static <T> Iterable<T> filter(Predicate<? super T> pred, Iterable<? extends T> a) {
        final List<T> processedElements = new ArrayList<T>();
        for (T item : a) {
            if (pred.apply(item)) {
                processedElements.add(item);
            }
        }
        return processedElements;
    }

    public static <T> Iterable<T> takeWhile(Predicate<? super T> pred, Iterable<? extends T> a) {
        final List<T> processedElements = new ArrayList<T>();
        for (T item : a) {
            if (pred.apply(item)) {
                processedElements.add(item);
            } else {
                break;
            }
        }
        return processedElements;
    }

    public static <T> Iterable<T> takeUnless(Predicate<? super T> pred, Iterable<? extends T> a) {
        return takeWhile(pred.not(), a);
    }

    public static <T, U> U foldl(Function2<? super U, ? super T, ? extends U> func,
                                 U accum,
                                 Iterable<? extends T> items) {
        for (T item : items) {
            accum = func.apply(accum, item);
        }
        return accum;
    }

    public static <T, U> U foldr(Function2<? super T, ? super U, ? extends U
            > func,
                                 U accum, Iterable<? extends T> items) {
        return foldrIter(func, accum, items.iterator());
    }

    private static <T, U> U foldrIter(Function2<? super T, ? super U, ? extends U> func,
                                      U accum,
                                      Iterator<? extends T> iter) {
        if (iter.hasNext()) {
            T value = iter.next();
            accum = foldrIter(func, accum, iter);
            return func.apply(value, accum);
        } else {
            return accum;
        }
    }
}
