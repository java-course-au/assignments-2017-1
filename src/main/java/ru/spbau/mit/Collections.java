package ru.spbau.mit;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public final class Collections {
    private Collections() {}

    public static <X, Y>
    List<Y> map(Iterable<X> container, Function1<? super X, Y> function) {
        List<Y> mappedList = new ArrayList<>();
        for (X x : container) {
            mappedList.add(function.apply(x));
        }

        return mappedList;
    }

    public static <X>
    List<X> filter(Iterable<X> container, Predicate<? super X> predicate) {
        List<X> filteredList = new ArrayList<>();

        for (X x : container) {
            if (predicate.apply(x)) {
                filteredList.add(x);
            }
        }

        return filteredList;
    }

    public static <X>
    List<X> takeWhile(Iterable<X> container, Predicate<? super X> predicate) {
        List<X> takenList = new ArrayList<>();

        for (X x : container) {
            if (!predicate.apply(x)) {
                break;
            }
            takenList.add(x);
        }

        return takenList;
    }

    public static <X>
    List<X> takeUnless(Iterable<X> container, Predicate<? super X> predicate) {
        return takeWhile(container, predicate.not());
    }

    public static <X, Y>
    Y foldr(Iterable<X> container, Function2<? super X, Y, Y> function2,
            Y initialValue) {
        List<X> tempList = new ArrayList<>();

        for (X x : container) {
            tempList.add(x);
        }

        Y partialResult = initialValue;
        ListIterator<X> listIterator = tempList.listIterator(tempList.size());
        while (listIterator.hasPrevious()) {
            partialResult = function2.apply(listIterator.previous(),
                                            partialResult);
        }

        return partialResult;
    }

    public static <X, Y>
    Y foldl(Iterable<X> container, Function2<Y, ? super X, Y> function2,
            Y initialValue) {
        Y partialResult = initialValue;

        for (X x : container) {
            partialResult = function2.apply(partialResult, x);
        }

        return partialResult;
    }
}
