package ru.spbau.mit;

import java.util.*;

public class MultisetImpl<E> extends AbstractCollection<E> implements Multiset<E> {

    private LinkedHashMap<E, List<E>> container = new LinkedHashMap<>();

    @Override
    public int count(Object element) {
        return 0;
    }

    @Override
    public Set<E> elementSet() {
        return null;
    }

    @Override
    public Set<? extends Entry<E>> entrySet() {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return container.keySet().iterator();
    }

    @Override
    public int size() {
        return 0;
    }
}
