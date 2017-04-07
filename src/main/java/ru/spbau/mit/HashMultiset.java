package ru.spbau.mit;

import java.util.*;

public class HashMultiset<E> extends AbstractSet<E> implements Multiset<E> {

    class EntryImpl<E> implements Entry<E> {
        private E element = null;
        private int count = 0;

        EntryImpl(E elem) {
            element = elem;
            count = 1;
        }

        public E getElement() {
            return element;
        }

        public int getCount() {
            return count;
        }
    }

    class EntrySetImpl<E> extends LinkedHashSet<EntryImpl<E>> {
        private HashMultiset<E> collection;

        EntrySetImpl(HashMultiset<E> ms) {
            collection = ms;
        }

        private Iterator<EntryImpl<E>> iteratorOld() {
            return super.iterator();
        }

        @Override
        public Iterator<EntryImpl<E>> iterator() {
            return new ESIterator(this);
        }

        class ESIterator implements Iterator<EntryImpl<E>>  {
            private EntrySetImpl<E> es;
            private Iterator<EntryImpl<E>> iter;
            private EntryImpl<E> entry;

            ESIterator(EntrySetImpl<E> entries) {
                es = entries;
                iter = entries.iteratorOld();
            }

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public EntryImpl<E> next() {
                entry = iter.next();
                return entry;
            }

            @Override
            public void remove() {
                while (entry.count >= 1) {
                    es.collection.removeWithoutEntrySet(entry.getElement());
                }
                iter.remove();
            }
        }
    }

    private LinkedHashMap<E, EntryImpl<E>> map = new LinkedHashMap<>();
    private LinkedHashSet<E> set = new LinkedHashSet<>();
    private EntrySetImpl<E> entrySet = new EntrySetImpl<>(this);
//    private LinkedHashSet<EntryImpl<E>> entrySet = new LinkedHashSet<EntryImpl<E>>();
    private int size = 0;

    @Override
    public boolean add(E e) {
        EntryImpl<E> entry = map.get(e);
        if (entry == null) {
            entry = new EntryImpl<E>(e);
            set.add(e);
            entrySet.add(entry);
            map.put(e, entry);
        } else {
            entry.count += 1;
        }
        size += 1;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return count(o) != 0;
    }

    public boolean removeWithoutEntrySet(Object o) {
        EntryImpl<E> entry = map.get(o);
        if (entry == null) {
            return false;
        }

        size -= 1;
        entry.count -= 1;
        if (entry.getCount() == 0) {
            set.remove(0);
            map.remove(o);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        EntryImpl<E> entry = map.get(o);
        if (entry == null) {
            return false;
        }

        size -= 1;
        entry.count -= 1;
        if (entry.getCount() == 0) {
            set.remove(0);
            entrySet.remove(entry);
            map.remove(o);
        }
        return true;
    }

    public int count(Object element) {
        EntryImpl<E> entry = map.get(element);
        if (entry == null) {
            return 0;
        }
        return entry.getCount();
    }

    public Set<E> elementSet() {
        return set;
    }

    public Set<? extends Entry<E>> entrySet() {
        return entrySet;
    }

    public Iterator<E> iterator() {
        return new MSIterator(this);
    }

    class MSIterator implements Iterator<E> {
        private HashMultiset<E> collection;
        private Iterator<EntryImpl<E>> iter;
        private EntryImpl<E> entry;
        private int curSize;

        MSIterator(HashMultiset<E> ms) {
            collection = ms;
            iter = collection.entrySet.iterator();
            entry = null;
            curSize = 0;
        }

        @Override
        public E next() {
            if (entry == null || curSize >= entry.getCount()) {
                entry = iter.next();
                curSize = 0;
            }
            curSize += 1;
            return entry.getElement();
        }

        @Override
        public boolean hasNext() {
            if (entry != null && curSize < entry.getCount()) {
                return true;
            }
            return iter.hasNext();
        }

        @Override
        public void remove() {
            collection.removeWithoutEntrySet(entry.getElement());
            curSize -= 1;
            if (entry.getCount() == 0) {
                entry.count = 0;
                entry = null;
                iter.remove();
            }
        }
    }

    public void clear() {
        set.clear();
        map.clear();
        entrySet.clear();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }
}
