package ru.spbau.mit;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SmartList<T> extends AbstractList<T> {
    private static final int ARRAY_SIZE = 5;
    private Object list = null;
    private int size = 0;

    public SmartList() {
    }

    public SmartList(T obj) {
        add(obj);
    }

    public SmartList(Collection<? extends T> objs) {
        addAll(objs);
    }

    @Override
    public T get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 1) {
            return (T) list;
        } else if (size <= ARRAY_SIZE) {
            return ((T[]) (list))[i];
        } else {
            return ((ArrayList<T>) (list)).get(i);
        }
    }

    @Override
    public T set(int i, T t) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        T oldValue = get(i);
        if (size == 1) {
            list = t;
        } else if (size <= ARRAY_SIZE) {
            ((T[]) list)[i] = t;
        } else {
            ((ArrayList<T>) list).set(i, t);
        }

        return oldValue;
    }

    @Override
    public boolean add(T t) {
        if (size == 0) {
            list = t;
        } else if (size == 1) {
            T oldValue = (T) list;
            T[] array = (T[]) (new Object[ARRAY_SIZE]);

            array[0] = oldValue;
            array[1] = t;

            list = array;
        } else if (size < ARRAY_SIZE) {
            T[] array = (T[]) list;
            array[size] = t;
        } else if (size == ARRAY_SIZE) {
            ArrayList<T> arrayList = new ArrayList<>(ARRAY_SIZE + 1);

            T[] array = (T[]) list;

            arrayList.addAll(Arrays.asList(array));
            arrayList.add(t);

            list = arrayList;
        } else {
            ((ArrayList<T>) list).add(t);
        }

        size++;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T remove(int i) {
        T oldValue = get(i);

        if (size == 1) {
            list = null;
        } else if (size == 2) {
            list = get((i + 1) % 2);
        } else if (size <= ARRAY_SIZE) {
            T[] array = (T[]) list;

            for (int j = 0; j < size; j++) {
                if (j > i) {
                    array[j - 1] = array[j];
                }
            }
        } else if (size == ARRAY_SIZE + 1) {
            T[] array = (T[]) new Object[ARRAY_SIZE];
            ArrayList<T> arrayList = (ArrayList<T>) list;

            for (int j = 0; j < size; j++) {
                if (j < i) {
                    array[j] = arrayList.get(j);
                }
                if (j > i) {
                    array[j - 1] = arrayList.get(j);
                }
            }

            list = array;
        } else {
            ((ArrayList<T>) list).remove(i);
        }

        size--;
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
