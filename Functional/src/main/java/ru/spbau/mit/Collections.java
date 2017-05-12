package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Collections {

    public static <T, R>
    List<R> map(Function1<? super T, R> f, Iterable<T> a) {
        List<R> res = new ArrayList<>();
        for (T item : a) {
            res.add(f.apply(item));
        }
        return res;
    }

    public static <T>
    List<T> filter(Predicate<? super T> p, Iterable<T> a) {
        List<T> result = new ArrayList<T>();
        for (T item : a) {
            if (p.apply(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static <T>
    List<T> takeWhile(Predicate<? super T> p, Iterable<T> a) {
        List<T> result = new ArrayList<>();
        for (T element : a) {
            if (!p.apply(element)) {
                break;
            }
            result.add(element);
        }
        return result;
    }

    public static <T>
    List<T> takeUnless(Predicate<? super T> p, Iterable<T> a) {
        return takeWhile(p.not(), a);
    }

    public static <T1, T2>
    T2 foldl(Function2<? super T2, ? super T1, T2> f, T2 begin, Iterable<T1> a) {
        T2 result = begin;
        for (T1 item : a) {
            result = f.apply(result, item);
        }
        return result;
    }

    public static <T1, T2>
    T2 foldr(Function2<? super T1, ? super T2, T2> f, T2 begin, Iterable<T1> a) {
        LinkedList<T1> list = new LinkedList<>();
        for (T1 item : a) {
            list.addFirst(item);
        }
        T2 result = begin;
        for (T1 item : list) {
            result = f.apply(item, result);
        }
        return result;
    }
}
