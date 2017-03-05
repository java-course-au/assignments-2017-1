package ru.spbau.mit;

import java.util.Arrays;

public class DictionaryImpl implements Dictionary {
    private static final int DEFAULT_CAPACITY = 4;
    private static final int CAPACITY_COEF = 4;

    private String[] key;
    private String[] value;
    private State[] state;
    private int size;
    private int freeNumber;

    DictionaryImpl() {
        clear();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(String key) {
        return state[getCell(key)] == State.BUSY;
    }

    @Override
    public String get(String key) {
        int id = getCell(key);
        if (state[id] == State.FREE) {
            return null;
        }
        return value[id];
    }

    @Override
    public String put(String key, String value) {
        int id = getCell(key);
        String oldValue = this.value[id];
        if (state[id] == State.FREE) {
            this.key[id] = key;
            size++;
            freeNumber--;
            oldValue = null;
        }
        state[id] = State.BUSY;
        this.value[id] = value;

        ensureCapacity();

        return oldValue;
    }

    @Override
    public String remove(String key) {
        int id = getCell(key);
        if (state[id] == State.FREE) {
            return null;
        }
        size--;
        state[id] = State.REMOVED;
        this.key[id] = null;
        String oldValue = value[id];
        value[id] = null;
        return oldValue;
    }

    @Override
    public void clear() {
        key = new String[DEFAULT_CAPACITY];
        value = new String[DEFAULT_CAPACITY];
        state = new State[DEFAULT_CAPACITY];
        freeAll();
    }

    private void freeAll() {
        Arrays.fill(state, State.FREE);
        size = 0;
        freeNumber = key.length;
    }

    private void ensureCapacity() {
        if ((key.length - freeNumber) * CAPACITY_COEF <= key.length) {
            return;
        }
        String[] oldValue = value;
        value = new String[value.length * 2];
        State[] oldState = state;
        state = new State[state.length * 2];
        String[] oldKey = key;
        key = new String[key.length * 2];
        freeAll();

        for (int id = 0; id < oldKey.length; id++) {
            if (oldState[id] == State.BUSY) {
                put(oldKey[id], oldValue[id]);
            }
        }
    }

    private int getCell(String s) {
        int id = getId(s);
        while (state[id] != State.FREE) {
            if (state[id] == State.BUSY && s.equals(key[id])) {
                break;
            }
            id++;
            if (id == key.length) {
                id = 0;
            }
        }
        return id;
    }

    private int getId(String s) {
        return (s.hashCode() % key.length + key.length) % key.length;
    }

    private enum State {
        FREE, REMOVED, BUSY
    }
}
