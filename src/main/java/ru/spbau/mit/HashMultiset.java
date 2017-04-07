package ru.spbau.mit;

import java.util.*;

public class HashMultiset<T> implements Multiset<T> {

    private LinkedHashMap<T, Integer> lhm = new LinkedHashMap<>();
    private int s = 0;
    @Override
    public int count(Object element) {
        if (contains(element)) {
            return lhm.get(element);
        }
        return 0;
    }

    @Override
    public Set<T> elementSet() {
        return lhm.keySet();
    }

    @Override
    public Set<? extends Entry<T>> entrySet() {
        return (Set<? extends Entry<T>>) lhm.keySet();
    }

    @Override
    public int size() {
        return s;
    }

    @Override
    public boolean isEmpty() {
        return lhm.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return lhm.keySet().contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private T current = null;
            private int pos = 0;
            private int acc = 0;
            @Override
            public boolean hasNext() {
                if (acc == s) {
                    return false;
                }
                return true;
            }

            @Override
            public T next() {
                Object[] objects = HashMultiset.this.toArray();
                acc++;
                pos++;
                return (T) objects[pos];
            }

            @Override
            public void remove() {
                HashMultiset.this.remove(current);
            }
        };
    }

    @Override
    public Object[] toArray() {
        ArrayList<T> out = new ArrayList<>();
        Iterator<T> iterator = lhm.keySet().iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            for (int i = 0; i < lhm.get(next); i++) {
                out.add(next);
            }
        }
        return out.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return (T1[]) toArray();
    }

    @Override
    public boolean add(T t) {
        if (contains(t)) {
            int count = lhm.get(t);
            lhm.put(t, count + 1);
            s++;
            return true;
        }
        lhm.put(t, 1);
        s++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            int count = lhm.get(o);
            if (count == 1) {
                lhm.remove(o);
            } else {
                lhm.remove(o);
                lhm.put((T) o, count - 1);
            }
            s--;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        Iterator<? extends T> iterator = collection.iterator();
        boolean flag = true;
        while (iterator.hasNext()) {
            if (!add(iterator.next())) {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        Iterator<?> iterator = collection.iterator();
        boolean flag = true;
        while (iterator.hasNext()) {
            if (!remove(iterator.hasNext())) {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {
        lhm.clear();

    }
}