package ru.spbau.mit;

import java.util.*;

public class Collections {

    public static <X, T> T foldr(Function2<? super X, ? super T, ? extends T> f, T ini, Iterable<X> xs) {
        return foldrHelper(f, ini, xs.iterator());
    }

    private static <X, T> T foldrHelper(Function2<? super X, ? super T, ? extends T> f, T ini, Iterator<X> it) {
        if (!it.hasNext()) {
            return ini;
        }
        return f.apply(it.next(), foldrHelper(f, ini, it));
    }

    public static <X, T> T foldl(Function2<? super X, ? super T, ? extends T> f, T ini, Iterable<X> xs) {
        return foldlHelper(f, ini, xs.iterator());
    }

    private static <X, T> T foldlHelper(Function2<? super X, ? super T, ? extends T> f, T ini, Iterator<X> it) {
        if (!it.hasNext()) {
            return ini;
        }
        return foldlHelper(f, f.apply(it.next(), ini), it);
    }

    // следующие методы можно реализовать через foldr и foldr, но будем делать через циклы, чтобы без рекурсии

    public static <T, R> Iterable<R> map(Function1<? super T, ? extends R> mapper, Iterable<T> xs) {
        List<R> list = new ArrayList<>();
        for (T el : xs) {
            list.add(mapper.apply(el));
        }
        return list;
    }

    public static <T> Iterable<T> filter(Predicate<? super T> tester, Iterable<T> xs) {
        List<T> list = new ArrayList<>();
        for (T el : xs) {
            if (tester.test(el)) {
                list.add(el);
            }
        }
        return list;
    }

    public static <T> Iterable<T> takeWhile(Predicate<? super T> tester, Iterable<T> xs) {
        List<T> list = new ArrayList<>();
        for (T el : xs) {
            if (!tester.test(el)) {
                break;
            }
            list.add(el);
        }
        return list;
    }

    public static <T> Iterable<T> takeUnless(Predicate<? super T> tester, Iterable<T> xs) {
        return takeWhile(tester.not(), xs);
    }

}
