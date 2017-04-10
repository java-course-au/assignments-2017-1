package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Collections {
    private Collections() {
    }

    public static <F, T> Iterable<T> map(Function1<? super F, ? extends T> f, Iterable<F> it) {
        List<T> mapped = new ArrayList<>();
        for (F element : it) {
            mapped.add(f.apply(element));
        }
        return mapped;
    }

    public static <T> Iterable<T> filter(Predicate<? super T> p, Iterable<T> it) {
        List<T> filtered = new ArrayList<>();
        for (T element : it) {
            if (p.apply(element)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    public static <T> Iterable<T> takeWhile(Predicate<? super T> p, Iterable<T> it) {
        List<T> taken = new ArrayList<>();
        for (T element : it) {
            if (!p.apply(element)) {
                break;
            }
            taken.add(element);
        }
        return taken;
    }

    public static <T> Iterable<T> takeUnless(Predicate<? super T> p, Iterable<T> it) {
        return takeWhile(p.not(), it);
    }

    public static <T, I> I foldr(Function2<? super T, ? super I, ? extends I> f, I init, Iterable<T> it) {
        LinkedList<T> elems = new LinkedList<>();
        for (T elem : it) {
            elems.addFirst(elem);
        }

        return foldl(f.flip(), init, elems);
    }

    public static <T, I> I foldl(Function2<? super I, ? super T, ? extends I> f, I init, Iterable<T> it) {
        for (T elem : it) {
            init = f.apply(init, elem);
        }
        return init;
    }
}
