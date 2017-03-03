package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {

    private static final int DEFAULT_HASH_SIZE = 100;

    private Node[] hashes;
    private int size;

    public DictionaryImpl() {
        clear();
    }

    public int size() {
        return size;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public String get(String key) {
        final Node node = getNode(key);
        return (node == null) ? null : node.value;
    }

    public String put(String key, String value) {
        final String old = remove(key);
        append(key, value);
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
            if (cur.next.key.equals(key)) {
                final String res = cur.next.value;
                cur.next = cur.next.next;
                size -= 1;
                return res;
            }
            cur = cur.next;
        }

        return null;
    }

    public void clear() {
        hashes = new Node[DEFAULT_HASH_SIZE];
        size = 0;
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
