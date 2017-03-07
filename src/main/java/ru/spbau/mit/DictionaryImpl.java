package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {
    private static final int START_NUM_BUCKETS = 47;
    private static final int BUCKET_SIZE = 10;
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

    private int getNumberOfBucket(String key) {
        return (Math.abs(key.hashCode())) % numBuckets;
    }

    private int getNumberOfBucket(String key, int newNumBuckets) {
        return (Math.abs(key.hashCode())) % newNumBuckets;
    }

    @Override
    public boolean contains(String key) {
        ArrayDict bucket = buckets[getNumberOfBucket(key)];
        return bucket.contains(key);
    }

    @Override
    public String get(String key) {
        ArrayDict bucket = buckets[getNumberOfBucket(key)];
        return bucket.find(key);
    }

    @Override
    public String put(String key, String value) {
        ArrayDict bucket = buckets[getNumberOfBucket(key)];
        String oldValue = bucket.replace(key, value);
        if (oldValue == null) {
            size++;

            if (bucket.getSize() == BUCKET_SIZE) {
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
                        getNumberOfBucket(key, newNumBuckets)];
                newBucket.add(key, value);
            }
        }

        numBuckets = newNumBuckets;
        buckets = newBuckets;
    }

    @Override
    public String remove(String key) {
        ArrayDict bucket = buckets[getNumberOfBucket(key)];
        String oldValue = bucket.remove(key);
        if (oldValue != null) {
            size--;
            if (size == numBuckets / DOWN_FACTOR_SIZE) {
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

    private class ArrayDict {
        private int size = 0;
        private String[] keys;
        private String[] values;

        ArrayDict() {
            size = 0;
            keys = new String[BUCKET_SIZE];
            values = new String[BUCKET_SIZE];
        }

        int getSize() {
            return size;
        }

        String getKey(int i) {
            return keys[i];
        }

        String getValue(int i) {
            return values[i];
        }

        void add(String key, String value) {
            keys[size] = key;
            values[size] = value;
            size++;
        }

        String replace(String key, String value) {
            for (int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    String oldValue = values[i];
                    values[i] = value;
                    return oldValue;
                }
            }

            add(key, value);
            return null;
        }

        boolean contains(String key) {
            for (int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    return true;
                }
            }
            return false;
        }

        String find(String key) {
            for (int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    return values[i];
                }
            }
            return null;
        }

        String remove(String key) {
            for (int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    keys[i] = keys[size - 1];
                    keys[size - 1] = null;

                    final String value = values[i];
                    values[i] = values[size - 1];
                    values[size - 1] = null;

                    size--;
                    return value;
                }
            }
            return null;
        }

        void clear() {
            size = 0;
            keys = new String[BUCKET_SIZE];
            values = new String[BUCKET_SIZE];
        }
    }
}
