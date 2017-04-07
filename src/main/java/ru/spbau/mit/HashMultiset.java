package ru.spbau.mit;

import java.util.*;

public class HashMultiset<E> extends AbstractCollection<E> implements Multiset<E> {
    private int size = 0;

    private LinkedHashMap<E, MultiSetEntry> map = new LinkedHashMap<>();
    private LinkedHashSet<MultiSetEntry> entrySet = new LinkedHashSet<>();
    private HashSet<E> elementSet = new HashSet<>();
    private Iterator iterator;


    class HashMultisetIterator implements Iterator {
        Iterator<MultiSetEntry> entrySetIt;
        Iterator<E> entryIt;

        HashMultisetIterator(Iterator<MultiSetEntry> f, Iterator<E> s) {
            entrySetIt = f;
            entryIt = s;
        }

        @Override
        public boolean hasNext() {
            return entryIt.hasNext();
        }

        @Override
        public E next() {
            if (!entryIt.hasNext()) {
                entryIt = entrySetIt.next().iterator();
            }
            return entryIt.next();
        }

        @Override
        public void remove() {

        }
    }

    private class MultiSetEntry implements Entry<E> {
        class MultisetIterator implements Iterator {
            Iterator<E> entryIt;

            MultisetIterator(Iterator<E> s) {
                entryIt = s;
            }

            @Override
            public boolean hasNext() {
                return entryIt.hasNext();
            }

            @Override
            public E next() {
                return entryIt.next();
            }

            @Override
            public void remove() {
                entryIt.remove();
            }
        }

        E value;
        private List<E> values = new ArrayList<>();

        MultiSetEntry(E value) {
            this.value = value;
            values.add(value);
        }

        @Override
        public E getElement() {
            return values.get(0);
        }

        @Override
        public int getCount() {
            return values.size();
        }

        void put(E e) {
            values.add(e);
        }

        void remove(Object e) {
            values.remove(e);
        }

        public Iterator<E> iterator() {
            return values.iterator();
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MultiSetEntry that = (MultiSetEntry) o;

            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

    }

    @Override
    public int count(Object element) {
        MultiSetEntry entry = map.get(element);
        return entry == null ? 0 : entry.getCount();
    }

    @Override
    public Set<E> elementSet() {
        return elementSet;
    }

    @Override
    public Set<? extends Entry<E>> entrySet() {
        return Collections.unmodifiableSet(entrySet);
    }

    @Override
    public Iterator<E> iterator() {
        return iterator == null ? new HashMultisetIterator(entrySet.iterator(), null) : iterator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        MultiSetEntry entry = map.get(e);
        if (entry == null) {
            MultiSetEntry newEntry = new MultiSetEntry(e);
            map.put(e, newEntry);
            entrySet.add(newEntry);
        } else {
            entry.put(e);
        }
        size++;
        elementSet.add(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        MultiSetEntry entry = map.get(o);
        if (entry == null) {
            return false;
        } else {
            entry.remove(o);
            size--;
            return true;
        }
    }

}
