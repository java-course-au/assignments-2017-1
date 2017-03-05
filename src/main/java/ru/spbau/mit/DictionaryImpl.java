package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {

    private static final int DEFAULT_HASH_SIZE = 100;
    private static final double REHASH_LEVEL = 0.75;
    private static final double RESIZE_FACTOR = 1.666;

    private Node[] hashes;
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

        final String old = remove(key);
        append(key, value);

        if (old == null && needRehash()) {
            rehash();
        }
        return old;
    }

    public String remove(String key) {
        Node cur = hashes[getIndex(key)];
        if (cur == null) {
            return null;
        } else if (cur.key.equals(key)) {
            hashes[getIndex(key)] = cur.next;
            size -= 1;
            return cur.value;
        }

        while (cur.next != null) {
            final Node next = cur.next;
            if (next.key.equals(key)) {
                cur.next = next.next;
                size -= 1;
                return next.value;
            }
            cur = next;
        }

        return null;
    }

    public void clear() {
        bucketNum = DEFAULT_HASH_SIZE;
        hashes = new Node[bucketNum];
        size = 0;
    }

    private boolean needRehash() {
        return size() > REHASH_LEVEL * bucketNum;
    }

    private void rehash() {
        final Node[] oldHashes = hashes;
        bucketNum *= RESIZE_FACTOR;
        hashes = new Node[bucketNum];
        size = 0;

        for (Node node : oldHashes) {
            while (node != null) {
                append(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(String key) {
        return Math.abs(key.hashCode()) % hashes.length;
    }

    private Node getNode(String key) {
        Node node = hashes[getIndex(key)];
        while (node != null && !node.key.equals(key)) {
            node = node.next;
        }
        return node;
    }

    private void append(String key, String value) {
        int idx = getIndex(key);
        hashes[idx] = new Node(hashes[idx], key, value);
        size += 1;
    }

    private class Node {
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
