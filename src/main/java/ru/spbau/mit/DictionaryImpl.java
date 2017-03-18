package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {
    private int capacity;
    private int size;
    private List[] buckets;
    private final int defaultCapacity = 1;

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
        return getNodeFromArrayByKey(key) != null;
    }

    @Override
    public String get(String key) {
        List.Node node = getNodeFromArrayByKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public String put(String key, String value) {
        String result = putUnbalanced(key, value);
        handleNumberOfBuckets();
        return result;
    }

    private String putUnbalanced(String key, String value) {
        if (key == null) {
            return null;
        }

        List bucket = getBucket(key);
        if (bucket == null) {
            bucket = createBucket(key);
        }

        List.Node prevVal = bucket.remove(key);
        bucket.push(key, value);

        if (prevVal == null) {
            size++;
            return null;
        }

        return prevVal.value;
    }

    private List getBucket(String key) {
        int index = getIndex(key);
        return buckets[index];
    }

    private List createBucket(String key) {
        int index = getIndex(key);
        buckets[index] = new List();
        return buckets[index];
    }

    private void handleNumberOfBuckets() {
        final int upperThreshold = capacity * 3 / 4;
        final int lowerThreshold = capacity / 8;
        if (size > upperThreshold) {
            rehash(capacity * 2);
        } else if (size < lowerThreshold) {
            rehash(capacity / 2);
        }
    }

    private void rehash(int newCapacity) {
        capacity = newCapacity;
        List[] prevBuckets = buckets;
        buckets = new List[capacity];
        size = 0;

        for (List bucket : prevBuckets) {
            if (bucket != null) {
                for (List.Node node = bucket.root; node != null; node = node.next) {
                    putUnbalanced(node.key, node.value);
                }
            }
        }
    }

    @Override
    public String remove(String key) {
        List bucket = getBucket(key);

        if (bucket == null) {
            return null;
        }

        List.Node node = bucket.remove(key);

        if (node != null) {
            size--;
            handleNumberOfBuckets();
            return node.value;
        }

        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    private List.Node getNodeFromArrayByKey(String key) {
        if (key == null) {
            return null;
        }

        List bucket = getBucket(key);

        return bucket == null ? null : bucket.getNodeByKey(key);
    }

    private int getIndex(String key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private static class List {
        private Node root;

        private void push(String key, String value) {
            root = new Node(key, value, root);
        }

        private Node remove(String key) {
            for (Node cur = root, prev = null; cur != null;  prev = cur, cur = cur.next) {
                if (cur.key.equals(key)) {
                    if (prev == null) {
                        root = cur.next;
                    } else if (cur.next == null) {
                        prev.next = null;
                    } else {
                        prev.next = cur.next;
                    }

                    return cur;
                }
            }

            return null;
        }

        private Node getNodeByKey(String key) {
            for (Node cur = root; cur != null; cur = cur.next) {
                if (cur.key.equals(key)) {
                    return cur;
                }
            }
            return null;
        }

        private static final class Node {
            private String key;
            private String value;
            private Node next;

            private Node(String key, String value, Node next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }
        }
    }
}
