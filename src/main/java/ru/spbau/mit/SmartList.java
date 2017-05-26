package ru.spbau.mit;

import java.util.*;


public class SmartList<T> extends AbstractList<T> {
    private static final int SIZE = 5;
    private Object buffer;
    private int size = 0;

    public SmartList() {

    }

    public SmartList(Collection<? extends T> c) {
        for (T t : c) {
            add(size, t);
        }
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 0) {
            buffer = element;
        } else if (size == 1) {
            Object tmp = buffer;
            buffer = new Object[SIZE];
            ((Object[]) buffer)[1 - index] = tmp;
            ((Object[]) buffer)[index] = element;
        } else if (size > 1 && size < SIZE) {
            Object[] tmp = (Object[]) buffer;
            for (int i = size - 1; i >= index; i++) {
                tmp[i + 1] = tmp[i];
            }
            ((Object[]) buffer)[index] = element;
        } else if (size == SIZE) {
            ArrayList<T> tmp = new ArrayList<>();
            Object[] buf = (Object[]) buffer;
            for (int i = 0; i < SIZE; i++) {
                tmp.add((T) buf[i]);
            }
            tmp.add(index, element);
            buffer = tmp;
        } else {
            ((ArrayList<T>) buffer).add(index, element);
        }
        size++;
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T out;
        if (size == 1) {
            out = (T) buffer;
            buffer = element;
        } else if (size > 1 && size <= SIZE) {
            out = (T) ((Object[]) buffer)[index];
            ((Object[]) buffer)[index] = element;
        } else {
            out = ((ArrayList<T>) buffer).get(index);
            ((ArrayList<T>) buffer).set(index, element);
        }
        return out;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return (T) buffer;
        } else if (size > 1 && size <= SIZE) {
            return (T) ((Object[]) buffer)[index];
        }
        return ((ArrayList<T>) buffer).get(index);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T out;
        if (size == 1) {
            out = (T) buffer;
            buffer = null;
        } else if (size > 1 && size <= SIZE) {
            out = (T) ((Object[]) buffer)[index];
            if (size == 2) {
                buffer = (T) ((Object[]) buffer)[1 - index];
            } else {
                for (int i = index + 1; i < size; i++) {
                    ((Object[]) buffer)[i - 1] = ((Object[]) buffer)[i];
                }
                ((Object[]) buffer)[size - 1] = null;
            }
        } else {
            if (size == SIZE + 1) {
                Object[] tmp = new Object[SIZE];
                out = ((ArrayList<T>) buffer).remove(index);
                ((ArrayList<T>) buffer).toArray(tmp);
                buffer = tmp;
            } else {
                out = ((ArrayList<T>) buffer).remove(index);
            }
        }
        size--;
        return out;
    }
}

