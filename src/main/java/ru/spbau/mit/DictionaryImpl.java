package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {

    private static final int MIN_BUCKET_NUM = 100;
    private static final double EXPAND_THRESHOLD = 0.75;
    private static final double SHRINK_THRESHOLD = 0.2;
    private static final double RESIZE_FACTOR = 1.666;

    private Node[] buckets;
    private int size;
    private int bucketNum;

    public DictionaryImpl() {
        clear();
    }

    public int size() {
        return size;
    }

    public boolean contains(String key) {
        return getNode(key) != null;
    }

    public String get(String key) {
        final Node node = getNode(key);
        return (node == null) ? null : node.value;
    }

    public String put(String key, String value) {
        if (key == null) {
            return null;
        }

        final Node oldNode = getNode(key);
        String oldValue = null;
        if (oldNode == null) {
            append(key, value);
        } else {
            oldValue = oldNode.value;
            oldNode.value = value;
        }

        tryRehash();
        return oldValue;
    }

    public String remove(String key) {
        Node cur = buckets[getIndex(key)];
        Node prev = null;
        if (cur == null) {
            return null;
        }

        while (cur != null) {
            if (cur.key.equals(key)) {
                if (prev == null) {
                    buckets[getIndex(key)] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size -= 1;
                tryRehash();
                return cur.value;
            }
            prev = cur;
            cur = cur.next;
        }

        return null;
    }

    public void clear() {
        bucketNum = MIN_BUCKET_NUM;
        buckets = new Node[bucketNum];
        size = 0;
    }

    private void tryRehash() {
        if (size() > EXPAND_THRESHOLD * bucketNum) {
            int newBucketNum = (int) (bucketNum * RESIZE_FACTOR);
            rehash(newBucketNum);
        } else if (size() < SHRINK_THRESHOLD * bucketNum) {
            int newBucketNum = Math.max(MIN_BUCKET_NUM, (int) (bucketNum / RESIZE_FACTOR));
            rehash(newBucketNum);
        }
    }

    private void rehash(int newBucketNum) {
        final Node[] oldBuckets = buckets;
        bucketNum = newBucketNum;
        buckets = new Node[bucketNum];
        size = 0;

        for (Node node : oldBuckets) {
            while (node != null) {
                append(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(String key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private Node getNode(String key) {
        Node node = buckets[getIndex(key)];
        while (node != null && !node.key.equals(key)) {
            node = node.next;
        }
        return node;
    }

    private void append(String key, String value) {
        int idx = getIndex(key);
        buckets[idx] = new Node(buckets[idx], key, value);
        size += 1;
    }

    private static class Node {
        private Node next;
        private String key;
        private String value;

        Node(Node next, String key, String value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
