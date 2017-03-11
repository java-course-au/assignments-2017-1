package ru.spbau.mit;

import java.util.Arrays;

public class DictionaryImpl implements Dictionary {
    private static final int DEFAULT_CAPACITY = 4;
    private static final int CAPACITY_COEF = 4;

    private String[] keys;
    private String[] values;
    private State[] states;
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
        return states[getCell(key)] == State.BUSY;
    }

    @Override
    public String get(String key) {
        int id = getCell(key);
        if (states[id] == State.FREE) {
            return null;
        }
        return values[id];
    }

    @Override
    public String put(String key, String value) {
        int id = getCell(key);
        String oldValue = this.values[id];
        if (states[id] == State.FREE) {
            this.keys[id] = key;
            size++;
            freeNumber--;
            oldValue = null;
        }
        states[id] = State.BUSY;
        this.values[id] = value;

        ensureCapacity();

        return oldValue;
    }

    @Override
    public String remove(String key) {
        int id = getCell(key);
        if (states[id] == State.FREE) {
            return null;
        }
        size--;
        states[id] = State.REMOVED;
        this.keys[id] = null;
        String oldValue = values[id];
        values[id] = null;
        return oldValue;
    }

    @Override
    public void clear() {
        keys = new String[DEFAULT_CAPACITY];
        values = new String[DEFAULT_CAPACITY];
        states = new State[DEFAULT_CAPACITY];
        freeAll();
    }

    private void freeAll() {
        Arrays.fill(states, State.FREE);
        size = 0;
        freeNumber = keys.length;
    }

    private void ensureCapacity() {
        if ((keys.length - freeNumber) * CAPACITY_COEF <= keys.length) {
            return;
        }
        String[] oldValue = values;
        values = new String[values.length * 2];
        State[] oldState = states;
        states = new State[states.length * 2];
        String[] oldKey = keys;
        keys = new String[keys.length * 2];
        freeAll();

        for (int id = 0; id < oldKey.length; id++) {
            if (oldState[id] == State.BUSY) {
                put(oldKey[id], oldValue[id]);
            }
        }
    }


    /**
     * Find where key is supposed to be in {@link #keys} array
     * @return position of the key or closest free position to the right
     */
    private int getCell(String key) {
        int id = getId(key);
        while (states[id] != State.FREE) {
            if (states[id] == State.BUSY && key.equals(keys[id])) {
                break;
            }
            id++;
            if (id == keys.length) {
                id = 0;
            }
        }
        return id;
    }

    private int getId(String key) {
        return (key.hashCode() % keys.length + keys.length) % keys.length;
    }

    private enum State {
        FREE, REMOVED, BUSY
    }
}
