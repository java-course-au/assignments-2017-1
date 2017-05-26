package ru.spbau.mit;

import java.util.*;

@SuppressWarnings("unchecked")
public class SmartList<E> extends AbstractList<E> {

    private int size = 0;
    private Object obj = null;

    public SmartList() {}

    public SmartList(Collection<E> col) {
        addAll(col);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 1) {
            return (E) obj;
        } else if (size < 5) {
            return (E) ((Object[]) obj)[index];
        }
        return (E) ((ArrayList<Object>) obj).get(index);
    }

    @Override
    public void clear() {
        obj = null;
        size = 0;
    }

    public boolean add(E e) {
        switch (size) {
            case 0:
                obj = e;
                break;

            case 1:
                Object[] temp = new Object[5];
                temp[0] = obj;
                temp[1] = e;
                obj = temp;
                break;

            case 2:case 3:case 4:
                temp = (Object[]) obj;
                temp[size] = e;
                obj = temp;
                break;

            case 5:
                List<Object> list = new ArrayList<>(Arrays.asList((Object[]) obj));
                list.add(e);
                obj = list;
                break;

            default:
                list = (List<Object>) obj;
                list.add(e);
                obj = list;
                break;
        }
        ++size;
        return true;
    }

    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E old;
        if (size == 1) {
            old = (E) obj;
            obj = element;
        } else if (size < 5) {
            Object[] temp = (Object[]) obj;
            old = (E) temp[index];
            temp[index] = element;
            obj = temp;
        } else {
            List<Object> temp = (List<Object>) obj;
            old = (E) temp.get(index);
            temp.set(index, element);
            obj = temp;
        }
        return old;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E old;
        switch (size) {
            case 1:
                old = (E) obj;
                obj = null;
                break;

            case 2:
                Object[] temp = (Object[]) obj;
                old = (E) temp[index];
                obj = temp[1 - index];
                break;

            case 3:case 4:case 5:
                temp = (Object[]) obj;
                old = (E) temp[index];
                for (int i = index; i < size - 1; ++i) {
                    Object t = temp[i];
                    temp[i] = temp[i + 1];
                    temp[i + 1] = t;
                }
                obj = temp;
                break;

            case 6:
                List<Object> list = (List<Object>) obj;
                old = (E) list.remove(index);
                obj = list.toArray();
                break;

            default:
                list = (List<Object>) obj;
                old = (E) list.remove(index);
                obj = list;
                break;

        }
        --size;
        return old;
    }
}
