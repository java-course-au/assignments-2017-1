package ru.spbau.mit;

import java.util.ArrayList;
import java.util.List;

public final class Collections {
    private Collections() {
    }

    ;

    public static <A1, R> Iterable<R> map(Function1<? super A1, ? extends R> func, Iterable<A1> collection) {
        List<R> mappedCollection = new ArrayList<>();
        for (A1 element : collection) {
            mappedCollection.add(func.apply(element));
        }
        return mappedCollection;
    }

    public static <A> Iterable<A> filter(Predicate<? super A> predicate, Iterable<A> collection) {
        List<A> filteredCollection = new ArrayList<>();
        for (A element : collection) {
            if (predicate.apply(element)) {
                filteredCollection.add(element);
            }
        }
        return filteredCollection;
    }

    public static <A> Iterable<A> takeWhile(Predicate<? super A> predicate, Iterable<A> collection) {
        List<A> elementsToTake = new ArrayList<>();
        for (A element : collection) {
            if (!predicate.apply(element)) {
                return elementsToTake;
            }
            elementsToTake.add(element);
        }

        return elementsToTake;
    }

    public static <A> Iterable<A> takeUnless(Predicate<? super A> predicate, Iterable<A> collection) {
        return takeWhile(predicate.not(), collection);
    }

    public static <A, B> B foldl(Function2<? super B, ? super A, ? extends B> func,
                                 B start, Iterable<A> collection) {
        B result = start;
        for (A element : collection) {
            result = func.apply(result, element);
        }
        return result;
    }

    public static <A, B> B foldr(Function2<? super A, ? super B, ? extends B> func,
                                 B start, Iterable<A> collection) {
        List<A> copy = new ArrayList<>();
        for (A element : collection) {
            copy.add(element);
        }
        java.util.Collections.reverse(copy);

        B result = start;
        for (A element : copy) {
            result = func.apply(element, result);
        }
        return result;
    }
}
