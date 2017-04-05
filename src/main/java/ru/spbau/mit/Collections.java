package ru.spbau.mit;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public final class Collections {

    private Collections() {}

    static <T, R> Iterable<R> map(Function1<R, ? super T> f, Iterable<T> collection) {
        List<R> res = new ArrayList<>();
        for (T it : collection) {
            res.add(f.apply(it));
        }
        return res;
    }

    static <T> Iterable<T> filter(Predicate<? super T> p, Iterable<T> collection) {
        List<T> res = new ArrayList<>();
        for (T it : collection) {
            if (p.apply(it)) {
                res.add(it);
            }
        }
        return res;
    }

    static <T> Iterable<T> takeWhile(Predicate<? super T> p, Iterable<T> collection) {
        List<T> res = new ArrayList<>();
        for (T it : collection) {
            if (!p.apply(it)) {
                break;
            }
            res.add(it);
        }
        return res;
    }

    static <T> Iterable<T> takeUnless(Predicate<? super T> p, Iterable<T> collection) {
        return takeWhile(p.not(), collection);
    }

    static <R, X> R foldr(Function2<R, ? super X, ? super R> f, R initial, Iterable<X> collection) {
        List<Function1<R, ? super R>> funcList = new ArrayList<>();
        for (X it : collection) {
            funcList.add(f.bind1(it));
        }
        ListIterator<Function1<R, ? super R>> lit = funcList.listIterator(funcList.size());
        R res = initial;
        while (lit.hasPrevious()) {
            res = lit.previous().apply(res);
        }
        return res;
    }

    static <R, X> R foldl(Function2<R, ? super R, ? super X> f, R initial, Iterable<X> collection) {
        List<Function1<R, ? super R>> funcList = new ArrayList<>();
        for (X it : collection) {
            funcList.add(f.bind2(it));
        }
        R res = initial;
        for (Function1<R, ? super R> func : funcList) {
            res = func.apply(res);
        }
        return res;
    }
}
