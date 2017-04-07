package ru.spbau.mit;

import java.util.*;

public class HashMultiset<E> implements Multiset<E> {
    private static final int START_NUM_BUCKETS = 50;
    private LinkedHashMap<E, Integer> data =
            new LinkedHashMap<>(START_NUM_BUCKETS);
    private LinkedHashSet<EntryHashMultiset<E>> entrySet =
            new LinkedHashSet<>(START_NUM_BUCKETS);
    private int size = 0;

    private static class EntryHashMultiset<E> implements Entry<E> {
        private final E element;
        private int count;

        EntryHashMultiset(E element, int count) {
            this.element = element;
            this.count = count;
        }

        @Override
        public E getElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }

        void decreaseCount() {
            count--;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof EntryHashMultiset) {
                EntryHashMultiset<E> entry = (EntryHashMultiset<E>) o;
                return entry.element == element;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return element.hashCode();
        }
    }

    private class HashMultisetIterator<E> implements Iterator<E> {
        private int currentCount = 0;
        private Iterator<EntryHashMultiset<E>> entrySetIterator;
        private EntryHashMultiset<E> curEntry = null;
        private LinkedHashMap<E, Integer> data;
        private LinkedHashSet<EntryHashMultiset<E>> entrySet;


        HashMultisetIterator(LinkedHashMap<E, Integer> data,
                LinkedHashSet<EntryHashMultiset<E>> entrySet) {
            this.data = data;
            this.entrySet = entrySet;
            entrySetIterator = entrySet.iterator();
            if (entrySetIterator.hasNext()) {
                curEntry = entrySetIterator.next();
            }
        }

        @Override
        public boolean hasNext() {
            return curEntry != null
                    && (currentCount < curEntry.count
                        || entrySetIterator.hasNext());
        }

        @Override
        public E next() {
            if (curEntry == null) {
                return null;
            }

            if (currentCount < curEntry.count) {
                currentCount++;
                return curEntry.element;
            }
            curEntry = entrySetIterator.next();
            currentCount = 0;

            return curEntry.element;
        }

        @Override
        public void remove() {
            if (curEntry.count == 0) {
                entrySetIterator.remove();
                data.remove(curEntry.element);
            } else {
                curEntry.decreaseCount();
                data.put(curEntry.element, curEntry.getCount());
            }
        }
    }

    private void increaseCountEntrySet(E element, int prevCount) {
        entrySet.remove(new EntryHashMultiset<E>(element, prevCount));
        entrySet.add(new EntryHashMultiset<E>(element, prevCount + 1));
    }

    private void decreaseCountEntrySet(E element, int prevCount) {
        entrySet.remove(new EntryHashMultiset<E>(element, prevCount));
        entrySet.add(new EntryHashMultiset<E>(element, prevCount - 1));
    }

    @Override
    public int count(Object element) {
        if (contains(element)) {
            return data.get(element);
        }

        return 0;
    }

    @Override
    public Set<E> elementSet() {
        return data.keySet();
    }

    @Override
    public Set<? extends Entry<E>> entrySet() {
        return entrySet;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return data.get(o) != null;
    }

    @Override
    public Iterator<E> iterator() {
        return new HashMultisetIterator<>(data, entrySet);
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator<E> it = iterator();
        for (int i = 0; i < size; i++) {
            array[i] = it.next();
        }

        return array;
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return null;
    }

    @Override
    public boolean add(E e) {
        size++;

        if (contains(e)) {
            Integer count = data.get(e);
            data.put(e, count + 1);

            increaseCountEntrySet(e, count);
            return true;
        }

        data.put(e, 1);
        entrySet.add(new EntryHashMultiset<E>(e, 1));
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            Integer count = data.get(o);
            if (count == 0) {
                data.remove(o);
                entrySet.remove(new EntryHashMultiset<>(o, 1));
                return true;
            }

            data.put((E) o, count - 1);
            decreaseCountEntrySet((E) o, count);

            size--;
            return true;
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
