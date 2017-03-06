package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;

public class DictionaryImpl implements Dictionary {
    private static final int TABLE_SIZE = 10;
    private static final double LOAD_FACTOR = 0.75;
    private static final int EXPANDING_SIZE = 2;

    private int sizeTable = TABLE_SIZE;
    private int sizeNotEmptyElem = 0;
    private LinkedList[] table = new LinkedList[sizeTable];

    public DictionaryImpl() {
        for (int i = 0; i < sizeTable; i++) {
            table[i] = new LinkedList();
        }
    }

    private int getHash(@NotNull String key) {
        return key.hashCode() % sizeTable;
    }

    @Override
    public boolean contains(@NotNull String key) {
        int index = getHash(key);
        return table[index].find(key) != null;
    }

    @Override
    public String get(@NotNull String key) {
        int index = getHash(key);
        return table[index].find(key);
    }

    @Override
    public String put(@NotNull String key, @NotNull String value) {
        int index = getHash(key);
        String oldValue = table[index].find(key);
        table[index].remove(key);
        table[index].insert(key, value);
        sizeNotEmptyElem = sizeNotEmptyElem + 1;
        rehash();
        return oldValue;
    }

    @Override
    public String remove(@NotNull String key) {
        int index = getHash(key);
        String oldValue = table[index].find(key);
        table[index].remove(key);
        sizeNotEmptyElem = sizeNotEmptyElem - 1;
        return oldValue;
    }

    private void rehash() {
        if (sizeNotEmptyElem / sizeTable < LOAD_FACTOR) {
            return;
        }

        LinkedList[] oldLists = table;

        sizeTable = sizeTable * EXPANDING_SIZE;
        table = new LinkedList[sizeTable];
        sizeNotEmptyElem = 0;

        for (int i = 0; i < sizeTable; i++) {
            table[i] = new LinkedList();
        }

        for (int i = 0; i < oldLists.length; i++) {
            Node current = oldLists[i].getNode();
            while (current != null) {
                put(current.keyNode, current.valueNode);
                current = oldLists[i].getNode();
            }
        }
    }

    @Override
    public void clear() {
        sizeNotEmptyElem = 0;
        for (int i = 0; i < sizeTable; i++) {
            table[i] = new LinkedList();
        }
    }

    @Override
    public int size() {
        return sizeNotEmptyElem;
    }

    private class LinkedList {
        private Node root;

        LinkedList() {
            root = null;
        }

        LinkedList(@NotNull String key, @NotNull String value) {
            root = new Node(key, value);
        }

        public void insert(@NotNull String key, @NotNull String value) {
            Node nodeInsert = new Node(key, value);
            if (root == null) {
                root = nodeInsert;
            } else {
                Node current = root;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = nodeInsert;
            }
        }

        public String find(@NotNull String key) {
            Node cuurentNode = root;
            while (cuurentNode != null) {
                if (cuurentNode.keyNode.equals(key)) {
                    return cuurentNode.valueNode;
                }
                cuurentNode = cuurentNode.next;
            }
            return null;
        }

        public void remove(@NotNull String key) {
            if (root == null) {
                return;
            }
            if (root.keyNode.equals(key)) {
                root = root.next;
                return;
            }
            Node currentNode = root;
            Node prevNode = null;

            while (currentNode != null && !currentNode.keyNode.equals(key)) {
                prevNode = currentNode;
                currentNode = currentNode.next;
            }

            if (currentNode == null) {
                return;
            }
            prevNode.next = currentNode.next;
        }

        public Node getNode() {
            Node current = root;
            if (current != null) {
                root = current.next;
            } else {
                root = null;
            }
            return current;
        }
    }

    private class Node {
        private String keyNode;
        private String valueNode;
        private Node next;

        Node() {
            keyNode = null;
            valueNode = null;
            next = null;
        }

        Node(@NotNull String key, @NotNull String value) {
            keyNode = key;
            valueNode = value;
            next = null;
        }
    }

}


