package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {

    private int capacity;
    private int size;
    private List[] buckets;
    private final int defaultCapacity = 100;

    public DictionaryImpl() {
        this.capacity = defaultCapacity;
        this.buckets = new List[capacity];
    }

    public DictionaryImpl(int capacity, List[] buckets) {
        this.capacity = capacity;
        this.buckets = buckets;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(String key) {

        if (key == null) {
            return false;
        }

        int hash = getHash(key);

        return buckets[hash] != null && buckets[hash].contains(key);
    }

    @Override
    public String get(String key) {

        if (contains(key)) {
            return getBucket(key).find(key);
        } else {
            return null;
        }
    }

    @Override
    public String put(String key, String value) {

        if (key == null) {
            return null;
        }

        if (contains(key)) {

            List bucket = getBucket(key);
            String prevVal = bucket.remove(key);
            bucket.push(key, value);
            return prevVal;

        } else {

            List bucket = getBucket(key);
            bucket.push(key, value);
            size++;
            handleOverflow();
            return null;

        }
    }

    private List getBucket(String key) {
        int hash = getHash(key);
        if (buckets[hash] == null) {
            buckets[hash] = new List();
        }
        return buckets[hash];
    }

    private void handleOverflow() {
        final int threshold = capacity * 3 / 4;
        if (size > threshold) {
            rehash();
        }
    }

    @Override
    public String remove(String key) {

        if (contains(key)) {
            size--;
            return getBucket(key).remove(key);
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    private int getHash(String key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private void rehash() {

        capacity *= 2;
        List[] prevBuckets = buckets;
        buckets = new List[capacity];
        size = 0;

        for (List bucket : prevBuckets) {
            if (bucket != null) {
                for (List.Node node = bucket.root; node != null; node = node.next) {
                    put(node.key, node.value);
                }
            }
        }
    }

    private class List {

        private Node root;

        void push(String key, String value) {
            root = new Node(key, value, root);
        }

        String remove(String key) {

            for (Node cur = root, prev = null; cur != null;  prev = cur, cur = cur.next) {
                if (cur.key.equals(key)) {
                    if (prev == null) {
                        root = cur.next;
                    } else if (cur.next == null) {
                        prev.next = null;
                    } else {
                        prev.next = cur.next;
                    }

                    return cur.value;
                }
            }

            return null;
        }

        String find(String key) {
            Node node = getNodeByKey(key);
            return node == null ? null : node.value;
        }

        boolean contains(String key) {

            return getNodeByKey(key) != null;
        }

        private Node getNodeByKey(String key) {
            for (Node cur = root; cur != null; cur = cur.next) {
                if (cur.key.equals(key)) {
                    return cur;
                }
            }
            return null;
        }

        class Node {

            private String key;
            private String value;
            private Node next;

            Node(String key, String value, Node next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }

        }
    }
}
