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
        return get(key) != null;
    }

    @Override
    public String get(String key) {
        int hash = getHash(key);
        if (buckets[hash] == null) {
            return null;
        }
        return buckets[hash].find(key);
    }

    @Override
    public String put(String key, String value) {
        int hash = getHash(key);
        if (buckets[hash] == null) {
            buckets[hash] = new List();
        }
        List bucket = buckets[hash];
        String prevVal = bucket.remove(key);
        bucket.push(key, value);

        if (prevVal == null) {
            size++;
            if (size > capacity * (2 + 1) / (2 + 2)) {
                rehash();
            }
        }
        return prevVal;
    }

    @Override
    public String remove(String key) {
        int hash = getHash(key);
        if (buckets[hash] == null) {
            return null;
        }


        List bucket = buckets[hash];
        String removed = bucket.remove(key);
        if (removed != null) {
            size--;
        }
        return removed;
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
            for (Node cur = root; cur != null; cur = cur.next) {
                if (cur.key.equals(key)) {
                    return cur.value;
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
