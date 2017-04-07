package ru.spbau.mit;

import java.util.*;

public class HashMultiset<E> extends AbstractCollection<E> implements Multiset<E> {

    private LinkedHashSet<HashSetEntry<E>> entrySet = new LinkedHashSet<>();
    private LinkedHashMap<E, HashSetEntry<E>> entryMap = new LinkedHashMap<>();

    private int size = 0;

    @Override
    public int count(Object element) {
        HashSetEntry<E> entry = entryMap.get(element);
        return entry == null ? 0 : entry.getCount();
    }

    @Override
    public Set<E> elementSet() {
        return entryMap.keySet();
    }

    @Override
    public Set<? extends Entry<E>> entrySet() {
        return entrySet;
    }

    @Override
    public boolean add(E e) {
        HashSetEntry<E> bucket = entryMap.get(e);
        if (bucket == null) {
            HashSetEntry<E> entry = new HashSetEntry<>(0, e);
            entrySet.add(entry);
            entryMap.put(e, entry);
        }

        ++size;

        HashSetEntry<E> entry = entryMap.get(e);
        entry.count++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        HashSetEntry<E> entry = entryMap.get(o);
        if (entry == null) {
            return false;
        }
        if (entry.getCount() == 0) {
            return false;
        }
        entry.count--;
        if (entry.count == 0) {
            entryMap.remove(o);
        }
        size--;
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new HashSetIterator<>(entryMap);
    }

    @Override
    public int size() {
        return size;
    }

    private static class HashSetIterator<E> implements Iterator<E> {

        private Iterator<E> outerIt;
        private LinkedHashMap<E, HashSetEntry<E>> outerCont;
        private int remains = 0;
        private E currentElement = null;

        HashSetIterator(LinkedHashMap<E, HashSetEntry<E>> cont) {
            outerIt = cont.keySet().iterator();
            outerCont = cont;
        }

        @Override
        public boolean hasNext() {
            return remains > 0 || outerIt.hasNext();
        }

        @Override
        public E next() {
            if (remains > 0) {
                remains--;
                return currentElement;
            }
            currentElement = outerIt.next();
            remains = outerCont.get(currentElement).getCount() - 1;
            return currentElement;
        }
    }

    public static class HashSetEntry<E> implements Entry<E> {

        private int count;
        private E element;

        public HashSetEntry(int count, E element) {
            this.count = count;
            this.element = element;
        }

        @Override
        public E getElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            HashSetEntry<?> that = (HashSetEntry<?>) o;

            if (count != that.count) {
                return false;
            }
            return element != null ? element.equals(that.element) : that.element == null;
        }

        @Override
        public int hashCode() {
            int result = count;
            final int prime = 31;
            result = prime * result + (element != null ? element.hashCode() : 0);
            return result;
        }
    }

    private static class EntrySet<E> extends AbstractSet<E> {
        @Override
        public Iterator<E> iterator() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    }
}
