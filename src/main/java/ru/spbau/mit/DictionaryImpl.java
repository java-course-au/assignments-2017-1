package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {
    private static final class DictionaryBucket {
        private static final class DictionaryNode {
            private String key;
            private String value;
            private DictionaryNode next;
            private DictionaryNode prev;

            private DictionaryNode(String key, String value) {
                this.key = key;
                this.value = value;
            }
        }

        private DictionaryNode head;
        private int size = 1;

        private DictionaryBucket(String key, String value) {
            head = new DictionaryNode(key, value);
        }

        private DictionaryNode find(String key) {
            DictionaryNode iter = head;

            while (iter != null && !iter.key.equals(key)) {
                iter = iter.next;
            }
            return iter;
        }

        private boolean contains(String key) {
            return find(key) != null;
        }

        private String put(String key, String value) {
            DictionaryNode node = find(key);

            if (node == null) {
                DictionaryNode newNode = new DictionaryNode(key, value);
                newNode.next = head;
                if (head != null) {
                    head.prev = newNode;
                }
                head = newNode;
                size++;
                return null;
            } else {
                String result = node.value;
                node.value = value;
                return result;
            }
        }

        private String get(String key) {
            DictionaryNode node = find(key);
            return node == null ? null : node.value;
        }

        private String remove(String key) {
            DictionaryNode node = find(key);

            if (node == null) {
                return null;
            }

            String result = node.value;
            if (node == head) {
                head = node.next;
            } else {
                node.prev.next = node.next;
                if (node.next != null) {
                    node.next.prev = node.prev;
                }
            }
            size--;
            return result;

        }

        private String[] getKeys() {
            String[] keys = new String[size];
            DictionaryNode iter = head;

            for (int i = 0; iter != null; i++) {
                keys[i] = iter.key;
                iter = iter.next;
            }

            return keys;
        }
    }

    private static final float LOAD_FACTOR_RATE = 4 / 3;
    private static final int INITIAL_CAPACITY = 11;

    private int capacity = INITIAL_CAPACITY;
    private int size = 0;
    private DictionaryBucket[] hashMap = new DictionaryBucket[capacity];

    private int getBucketIndex(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    private DictionaryBucket getBucket(String key) {
        int index = getBucketIndex(key);
        return hashMap[index];
    }

    private float maxLoadFactor() {
        return LOAD_FACTOR_RATE * capacity;
    }

    private void rehash() {
        capacity *= 2;
        DictionaryBucket[] oldHashMap = hashMap;
        hashMap = new DictionaryBucket[capacity];
        size = 0;

        for (DictionaryBucket bucket : oldHashMap) {
            if (bucket != null) {
                String[] keys = bucket.getKeys();
                for (String key : keys) {
                    put(key, bucket.get(key));
                }
            }
        }
    }

    /**
     * @return the number of keys
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @param key
     * @return true iff the dictionary contains `key`
     */
    @Override
    public boolean contains(String key) {
        DictionaryBucket bucket = getBucket(key);
        return bucket != null && bucket.contains(key);
    }

    /**
     * @param key
     * @return mapping from the given key to it's value, or null, if the dictionary
     * does not contain the key
     */
    @Override
    public String get(String key) {
        DictionaryBucket bucket = getBucket(key);
        return bucket == null ? null : bucket.get(key);
    }

    /**
     * Put new key-value pair into the dictionary,
     * or update the value for the given key if it already exists
     *
     * @param key
     * @param value
     * @return if the dictionary already contains the key, return an old value,
     * otherwise return null
     */
    @Override
    public String put(String key, String value) {
        if (size >= maxLoadFactor()) {
            rehash();
        }

        int index = getBucketIndex(key);
        DictionaryBucket bucket = hashMap[index];

        if (bucket == null) {
            hashMap[index] = new DictionaryBucket(key, value);
            size++;
            return null;
        } else {
            String result = bucket.put(key, value);
            if (result == null) {
                size++;
            }
            return result;
        }
    }

    /**
     * Remove the key-value from the dictionary if it exists there
     *
     * @param key
     * @return an associated value to the key or null if the dictionary
     * doesn't contain the key
     */
    @Override
    public String remove(String key) {
        DictionaryBucket bucket = getBucket(key);

        if (bucket == null) {
            return null;
        } else {
            String result = bucket.remove(key);
            if (result != null) {
                size--;
            }
            return result;
        }
    }

    /**
     * Remove all key-value pairs from the dictionary
     */
    @Override
    public void clear() {
        capacity = INITIAL_CAPACITY;
        hashMap = new DictionaryBucket[capacity];
        size = 0;
    }
}
