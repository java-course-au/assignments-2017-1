package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {
    private static final int START_SIZE = 32;
    private int sizeMap = START_SIZE;
    private Array[] hashMap = new Array[START_SIZE];
    private int countElements = 0;

    @Override
    public int size() {
        return countElements;
    }
    @Override
    public boolean contains(String key) {
        int hash = Math.abs(key.hashCode()) % sizeMap;
        return hashMap[hash] != null && hashMap[hash].contains(key);
    }

    @Override
    public String get(String key) {
        int hash = Math.abs(key.hashCode()) % sizeMap;
        if (hashMap[hash] == null) {
            return null;
        }
        return hashMap[hash].getValue(key);
    }

    @Override
    public String put(String key, String value) {
        int hash = Math.abs(key.hashCode()) % sizeMap;
        if (hashMap[hash] == null) {
            hashMap[hash] = new Array();
            hashMap[hash].add(key, value);
            countElements++;
            return null;
        }
        if (hashMap[hash].contains(key)) {
            String out = hashMap[hash].getValue(key);
            hashMap[hash].replace(key, value);
            return out;
        }
        if (!hashMap[hash].add(key, value)) {
            rehash();
            return put(key, value);
        }
        countElements++;
        return null;
    }

    @Override
    public String remove(String key) {
        int hash = Math.abs(key.hashCode()) % sizeMap;
        if (hashMap[hash] == null || !hashMap[hash].contains(key)) {
            return null;
        }
        countElements--;
        return hashMap[hash].remove(key);
    }

    @Override
    public void clear() {
        hashMap = new Array[sizeMap];
        countElements = 0;
    }

    private void rehash() {
        Array[] newHashMap = new Array[sizeMap * 2];
        Array[] oldHashMap = hashMap;
        int oldSizeMap = sizeMap;
        sizeMap *= 2;
        hashMap = newHashMap;
        for (int i = 0; i < oldSizeMap; i++) {
            if (oldHashMap[i] != null) {
                String[] keys = oldHashMap[i].getKeys();
                if (keys != null) {
                    for (String key : keys) {
                        put(key, oldHashMap[i].getValue(key));
                    }
                }
            }
        }
    }

    private class Array {
        private static final int SIZE = 8;
        private Node[] bucket = new Node[SIZE];
        private int count = 0;

        public boolean add(String key, String value) {
            if (count > SIZE) {
                return false;
            }
            for (int i = 0; i < SIZE; i++) {
                if (bucket[i] == null) {
                    bucket[i] = new Node(key, value);
                    count++;
                    break;
                }
            }
            return true;
        }

        public boolean replace(String key, String newValue) {
            for (int i = 0; i < SIZE; i++) {
                if (bucket[i] != null && bucket[i].key.equals(key)) {
                    bucket[i].value = newValue;
                    return true;
                }
            }
            return false;
        }
        public String remove(String key) {
            for (int i = 0; i < SIZE; i++) {
                if (bucket[i].key.equals(key)) {
                    String out = bucket[i].value;
                    bucket[i] = null;
                    count--;
                    return out;
                }
            }
            return null;
        }

        public String getValue(String key) {
            for (int i = 0; i < SIZE; i++) {
                if (bucket[i].key.equals(key)) {
                    return bucket[i].value;
                }
            }
            return null;
        }

        public boolean contains(String key) {
            return getValue(key) != null;
        }

        public String[] getKeys() {
            if (count == 0) {
                return null;
            }
            String[] out = new String[count];
            int currentIndex = 0;
            for (int i = 0; i < SIZE; i++) {
                if (bucket[i] != null) {
                    out[currentIndex] = bucket[i].key;
                    currentIndex++;
                }
            }
            return out;
        }
        private class Node {
            private String key;
            private String value;
            Node(String key, String value) {
                this.key = key;
                this.value = value;
            }
        }

    }
}
