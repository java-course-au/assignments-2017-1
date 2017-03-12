package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DictionaryImpl implements Dictionary {

    private static final int TABLE_SIZE = 10;
    private static final double LOAD_FACTOR = 0.75;
    private static final int EXPANDING_SIZE = 2;

    private LinkedList[] table = new LinkedList[TABLE_SIZE];
    private int size = 0;

    public DictionaryImpl() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = new LinkedList();
        }
    }

    @Override
    public boolean contains(@NotNull String key) {
        int index = getHash(key);
        return table[index].find(key) != null;
    }

    @Override
    @Nullable
    public String get(@NotNull String key) {
        int index = getHash(key);
        return table[index].find(key);
    }

    @Override
    @Nullable
    public String put(@NotNull String key, @NotNull String value) {
        int index = getHash(key);
        String oldValue = table[index].insert(key, value);
        if (oldValue == null) {
            size++;
        }
        rehash();
        return oldValue;
    }

    @Override
    @Nullable
    public String remove(@NotNull String key) {
        int index = getHash(key);
        String oldValue = table[index].remove(key);
        if (oldValue != null) {
            size--;
        }
        return oldValue;
    }

    @Override
    public void clear() {
        size = 0;
        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = new LinkedList();
        }
    }

    @Override
    public int size() {
        return size;
    }

    private int getHash(@NotNull String key) {
        return key.hashCode() % table.length;
    }

    private void rehash() {
        if (size / table.length < LOAD_FACTOR) {
            return;
        }

        LinkedList[] oldLists = table;

        int newSizeTable = table.length * EXPANDING_SIZE;
        table = new LinkedList[newSizeTable];
        size = 0;

        for (int i = 0; i < newSizeTable; i++) {
            table[i] = new LinkedList();
        }

        update(oldLists);
    }

    private void update(@NotNull final LinkedList[] oldLists) {
        for (int i = 0; i < oldLists.length; i++) {
            Node current = oldLists[i].root;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private static class LinkedList {
        private Node root;

        public LinkedList() {
            root = null;
        }

        @Nullable
        public String insert(@NotNull String key, @NotNull String value) {
            String oldValue = null;
            Node nodeInsert = new Node(key, value);

            if (root == null) {
                root = nodeInsert;
                return oldValue;
            }

            Node currentNode = root;
            Node prevNode = null;

            while (currentNode != null) {

                if (currentNode.key.equals(key)) {
                    oldValue = currentNode.value;
                    currentNode.value = value;
                    return oldValue;
                }

                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = nodeInsert;
            return oldValue;
        }

        @Nullable
        public String find(@NotNull String key) {
            Node cuurentNode = root;
            while (cuurentNode != null) {
                if (cuurentNode.key.equals(key)) {
                    return cuurentNode.value;
                }
                cuurentNode = cuurentNode.next;
            }
            return null;
        }

        @Nullable
        public String remove(@NotNull String key) {
            String oldValue = null;
            if (root == null) {
                return oldValue;
            }
            if (root.key.equals(key)) {
                oldValue = root.key;
                root = root.next;
                return oldValue;
            }
            Node currentNode = root;
            Node prevNode = null;

            while (currentNode != null && !currentNode.key.equals(key)) {
                prevNode = currentNode;
                currentNode = currentNode.next;
            }

            if (currentNode == null) {
                return oldValue;
            }
            oldValue = currentNode.value;
            prevNode.next = currentNode.next;
            return oldValue;
        }
    }

    private static class Node {
        private String key;
        private String value;
        private Node next;

        public Node(@NotNull String key, @NotNull String value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}


