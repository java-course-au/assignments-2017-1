package ru.spbau.mit;


import java.util.*;

public class SmartList<E> extends AbstractList<E> implements List<E> {
    private static final int MAX_ARRAY_SIZE = 6;
    private int size = 0;
    private Object holder = null;

    public SmartList() {
    }

    public SmartList(Collection<? extends E> collection) {
        for (E elem : collection) {
            add(elem);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        if (size == 0) {
            holder = e;
        } else if (size == 1) {
            Object[] newHolder = new Object[MAX_ARRAY_SIZE];
            newHolder[0] = holder;
            newHolder[1] = e;
            holder = newHolder;
        } else if (size < MAX_ARRAY_SIZE) {
            int nextIndex = size;
            ((Object[]) holder)[nextIndex] = e;
        } else if (size == MAX_ARRAY_SIZE) {
            List<E> newHolder = new ArrayList<>();
            Collections.addAll(newHolder, ((E[]) holder));
            newHolder.add(e);
            holder = newHolder;
        } else {
            ((ArrayList<E>) holder).add(e);
        }

        size += 1;
        return true;
    }

    @Override
    public E remove(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }

        E returnValue = null;
        if (size == 1) {
            returnValue = (E) holder;
            holder = null;
        } else if (size == 2) {
            returnValue = (E) ((Object[]) holder)[index];
            holder = ((Object[]) holder)[1 - index];
        } else if (size == MAX_ARRAY_SIZE) {
            returnValue = ((ArrayList<E>) holder).remove(index);
            holder = ((ArrayList<E>) holder).toArray();
        } else if (size < MAX_ARRAY_SIZE) {
            Object[] newHolder = new Object[size - 1];
            int j = 0;
            for (int i = 0; i < size; i++) {
                if (i != index) {
                    newHolder[j] = ((Object[]) holder)[i];
                    j++;
                } else {
                    returnValue = (E) ((Object[]) holder)[i];
                }
            }

            holder = newHolder;
        } else {
            returnValue = ((ArrayList<E>) holder).remove(index);
        }

        size--;
        return returnValue;
    }

    @Override
    public E get(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (size == 1) {
            return (E) holder;
        } else if (size < MAX_ARRAY_SIZE) {
            return ((E[]) holder)[index];
        } else {
            return ((ArrayList<E>) holder).get(index);
        }
    }

    @Override
    public E set(int index, E element) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }

        E returnValue;
        if (size == 1) {
            returnValue = (E) holder;
            holder = element;
        } else if (size < MAX_ARRAY_SIZE) {
            returnValue = ((E[]) holder)[index];
            ((E[]) holder)[index] = element;
        } else {
            returnValue = ((ArrayList<E>) holder).set(index, element);
        }

        return returnValue;
    }

    @Override
    public int size() {
        return size;
    }
}
