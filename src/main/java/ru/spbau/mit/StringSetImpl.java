package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private static class Trie {

        private boolean isTerminated = false;
        private int howManyStartsWithPrefix = 0;
        private Trie[] nodes = new Trie[SYMBOL_COUNT];
    }

    private static final int START_INDEX = 26;
    private static final int SYMBOL_COUNT = 52;
    private Trie root;

    public StringSetImpl() {
        root = new Trie();
    }
    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        Trie node = root;
        for (int i = 0; i < element.length(); i++) {
            int index = getIndex(element.charAt(i));
            if (node.nodes[index] == null) {
                node.nodes[index] = new Trie();
            }
            node = node.nodes[index];
            node.howManyStartsWithPrefix++;
        }
        node.isTerminated = true;
        root.howManyStartsWithPrefix++;
        return true;
    }

    @Override
    public boolean contains(String element) {
        Trie node = traverse(element);
        return node != null && node.isTerminated;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        Trie next = root;
        Trie prev = root;
        int index = 0;
        for (int i = 0; i < element.length(); i++) {
            index = getIndex(element.charAt(i));
            prev = next;
            next = next.nodes[index];
            next.howManyStartsWithPrefix--;
            if (next.howManyStartsWithPrefix == 0) {
                prev.nodes[index] = null;
            }
        }
        if (next != prev && next.howManyStartsWithPrefix == 1) {
            prev.nodes[index] = null;
        } else {
            next.isTerminated = false;
            root.howManyStartsWithPrefix--;
        }
        return true;
    }

    @Override
    public int size() {
        return root.howManyStartsWithPrefix;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Trie node = traverse(prefix);
        return node == null ? 0 : node.howManyStartsWithPrefix;
    }

    private static int getIndex(char symbol) {
        if (symbol >= 'a' && symbol <= 'z') {
            return START_INDEX + (int) symbol - (int) 'a';
        }
        return (int) symbol - (int) 'A';
    }

    private Trie traverse(String element) {
        Trie node = root;
        for (int i = 0; i < element.length(); i++) {
            if (node == null) {
                return null;
            }
            node = node.nodes[getIndex(element.charAt(i))];
        }
        return node;
    }
}
