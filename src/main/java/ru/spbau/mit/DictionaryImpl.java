package ru.spbau.mit;

import java.util.Arrays;

public class DictionaryImpl implements Dictionary {
    private static final int START_NUM_BUCKETS = 47;
    private static final int START_BUCKET_SIZE = 20;
    private static final double LOAD_LEVEL = 0.9;
    private static final double UP_FACTOR = 2.0;
    private static final double DOWN_FACTOR = 0.5;
    private static final int DOWN_FACTOR_SIZE = 4;
    private int numBuckets = START_NUM_BUCKETS;
    private ArrayDict[] buckets = new ArrayDict[numBuckets];
    private int size = 0;

    {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayDict();
        }
    }

    @Override
    public int size() {
        return size;
    }

    private int getIndexOfBucket(String key) {
        return (Math.abs(key.hashCode())) % numBuckets;
    }

    private int getIndexOfBucket(String key, int newNumBuckets) {
        return (Math.abs(key.hashCode())) % newNumBuckets;
    }

    @Override
    public boolean contains(String key) {
        ArrayDict bucket = buckets[getIndexOfBucket(key)];
        return bucket.contains(key);
    }

    @Override
    public String get(String key) {
        ArrayDict bucket = buckets[getIndexOfBucket(key)];
        return bucket.find(key);
    }

    @Override
    public String put(String key, String value) {
        ArrayDict bucket = buckets[getIndexOfBucket(key)];
        String oldValue = bucket.replace(key, value);
        if (oldValue == null) {
            size++;

            int capacity = numBuckets * START_BUCKET_SIZE;
            if (size / capacity >= LOAD_LEVEL) {
                rehash(UP_FACTOR);
            }
        }

        return oldValue;
    }

    private void rehash(double factor) {
        int newNumBuckets = (int) (numBuckets * factor);
        ArrayDict[] newBuckets = new ArrayDict[newNumBuckets];

        for (int i = 0; i < newNumBuckets; i++) {
            newBuckets[i] = new ArrayDict();
        }

        for (int i = 0; i < numBuckets; i++) {
            ArrayDict bucket = buckets[i];
            for (int j = 0; j < bucket.getSize(); j++) {
                String key = bucket.getKey(j);
                String value = bucket.getValue(j);
                ArrayDict newBucket = newBuckets[
                        getIndexOfBucket(key, newNumBuckets)];
                newBucket.add(key, value);
            }
        }

        numBuckets = newNumBuckets;
        buckets = newBuckets;
    }

    @Override
    public String remove(String key) {
        ArrayDict bucket = buckets[getIndexOfBucket(key)];
        String oldValue = bucket.remove(key);
        if (oldValue != null) {
            size--;
            if (size > START_NUM_BUCKETS
                    && size == numBuckets / DOWN_FACTOR_SIZE) {
                rehash(DOWN_FACTOR);
            }
        }
        return oldValue;
    }

    @Override
    public void clear() {
        for (ArrayDict bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }

    private static class ArrayDict {
        private int size = 0;
        private int capacity = START_BUCKET_SIZE;
        private String[] keys = new String[START_BUCKET_SIZE];
        private String[] values = new String[START_BUCKET_SIZE];

        private int getSize() {
            return size;
        }

        private void add(String key, String value) {
            keys[size] = key;
            values[size] = value;

            size++;

            if (size == capacity) {
                resize(UP_FACTOR);
            }
        }

        private void resize(double factor) {
            capacity = (int) (capacity * factor);
            keys = Arrays.copyOf(keys, capacity);
            values = Arrays.copyOf(values, capacity);
        }

        private String getKey(int i) {
            return keys[i];
        }

        private String getValue(int i) {
            return values[i];
        }

        private int traverse(String key) {
            for (int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    return i;
                }
            }

            return -1;
        }

        private String replace(String key, String value) {
            int index = traverse(key);
            if (index != -1) {
                String oldValue = values[index];
                values[index] = value;
                return oldValue;
            }

            add(key, value);
            return null;
        }

        private boolean contains(String key) {
            return traverse(key) != -1;
        }

        private String find(String key) {
            int index = traverse(key);
            return index != -1 ? values[index] : null;
        }

        private String remove(String key) {
            int index = traverse(key);

            if (index != -1) {
                keys[index] = keys[size - 1];
                keys[size - 1] = null;

                final String value = values[index];
                values[index] = values[size - 1];
                values[size - 1] = null;

                size--;

                if (size > START_BUCKET_SIZE
                        && size == capacity / DOWN_FACTOR_SIZE) {
                    resize(DOWN_FACTOR);
                }
                return value;
            }

            return null;
        }

        private void clear() {
            size = 0;
            keys = new String[START_BUCKET_SIZE];
            values = new String[START_BUCKET_SIZE];
        }
    }
}
