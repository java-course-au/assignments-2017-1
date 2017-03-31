package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Collections {
    private Collections() {
    }

    public static <F, T> Iterable<T> map(Function1<? super F, T> f, Iterable<F> c) {
        List<T> mapped = new ArrayList<>();
        for (F element : c) {
            mapped.add(f.apply(element));
        }
        return mapped;
    }

    public static <T> Iterable<T> filter(Predicate<? super T> p, Iterable<T> c) {
        List<T> filtered = new ArrayList<>();
        for (T element : c) {
            if (p.apply(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <T> Iterable<T> takeWhile(Predicate<? super T> p, Iterable<T> c) {
        List<T> taken = new ArrayList<>();
        for (T element : c) {
            if (!p.apply(element)) {
                break;
            }
            taken.add(element);
        }
        return taken;
    }

    public static <T> Iterable<T> takeUnless(Predicate<? super T> p, Iterable<T> c) {
        return takeWhile(p.not(), c);
    }

    public static <T, I> I foldr(Function2<? super T, ? super I, ? extends I> f, I ini, Iterable<T> c) {
        LinkedList<T> elems = new LinkedList<>();
        for (T elem : c) {
            elems.addFirst(elem);
        }

        return foldl(f.flip(), ini, elems);
    }

    public static <T, I> I foldl(Function2<? super I, ? super T, ? extends I> f, I ini, Iterable<T> c) {
        for (T elem : c) {
            ini = f.apply(ini, elem);
        }
        return ini;
    }
}
