package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Iterator;

final class Collections {
    private Collections() {}
    static <T, R> Iterable<R> map(Functional1<? super T, R> f, Iterable<T> c) {
        ArrayList<R> out = new ArrayList<>();
        for (T i : c) {
            out.add(f.apply(i));
        }
        return out;
    }

    static <T> Iterable<T> filter(Predicate<? super T> f, Iterable<T> c) {
        ArrayList<T> out = new ArrayList<>();
        for (T i : c) {
            if (f.apply(i)) {
                out.add(i);
            }
        }
        return out;
    }

    static <T> Iterable<T> takeWhile(Predicate<? super T> f, Iterable<T> c) {
        ArrayList<T> out = new ArrayList<>();
        for (T i: c) {
            if (!f.apply(i)) {
                break;
            }
            out.add(i);
        }
        return out;
    }

    static <T> Iterable<T> takeUnless(Predicate<? super T> f, Iterable<T> c) {
        return takeWhile(f.not(), c);
    }

    static <X, Y> X foldl(Functional2<? super X, ? super Y, X> f, X start, Iterable<Y> c) {
        X out = start;
        for (Y i : c) {
            out = f.apply(out, i);
        }
        return out;
    }

    static <X, Y> Y foldr(Functional2<? super X, ? super Y, Y> f, Y start, Iterable<X> c) {
        return foldrIterator(f, start, c.iterator());
    }

    private static <X, Y> Y foldrIterator(Functional2<? super X, ? super Y, Y> f, Y start, Iterator<X> c) {
        if (!c.hasNext()) {
            return start;
        }
        return f.apply(c.next(), foldrIterator(f, start, c));
    }
}
