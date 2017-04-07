package ru.spbau.mit;

import java.util.*;

public class HashMultiset<E> implements Multiset<E> {

    private LinkedHashMap<E, EntryImpl<E>> linkedHashMap = new LinkedHashMap<>();

    class EntryImpl<V> implements Entry<V> {

        private LinkedList<V> linkedList = new LinkedList<>();

        @Override
        public V getElement() {
            return linkedList.getFirst();
        }

        @Override
        public int getCount() {
            return linkedList.size();
        }

    }
    @Override
    public int count(Object element) {
        return linkedHashMap.get(element).getCount();
    }

    @Override
    public Set elementSet() {
        return linkedHashMap.keySet();
    }

    @Override
    public Set<? extends Entry> entrySet() {
        return linkedHashMap.;
    }

    @Override
    public int size() {
        return linkedHashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return linkedHashMap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return linkedHashMap.containsKey(o);
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        if (linkedHashMap.containsKey(o)) {
            linkedHashMap.get(o).linkedList.add((E) o);
            return true;
        }
        linkedHashMap.put((E) o, new EntryImpl<E>());
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (linkedHashMap.containsKey(o)) {
            return linkedHashMap.get(o).linkedList.remove(o);
        }
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        for (Object item : c) {
            linkedHashMap.put((E) item, new EntryImpl<E>());
        }
        return true;
    }

    @Override
    public void clear() {
        linkedHashMap.clear();
    }

    @Override
    public boolean retainAll(Collection c) {
        Collection<EntryImpl<E>> temp = linkedHashMap.values();
        for (EntryImpl<E> item : temp) {
            if (c.contains(item.getElement())) {
                linkedHashMap.remove(item.getElement());
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection c) {
        Collection<EntryImpl<E>> temp = linkedHashMap.values();
        for (EntryImpl<E> item : temp) {
            if (!c.contains(item.getElement())) {
                linkedHashMap.remove(item.getElement());
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        Collection<EntryImpl<E>> temp = linkedHashMap.values();
        for (Object item : c) {
            if (!linkedHashMap.containsKey(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

}
