package ru.spbau.mit;

import java.util.function.Function;

public class StringSetImpl implements StringSet {
    private static final int ALPHABET_SIZE = 52;

    private int size = 0;
    private Node root = new Node();

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        Node lastNode = createPath(element);
        lastNode.isTerminal = true;
        size++;
        return true;
    }

    @Override
    public boolean contains(String element) {
        Node lastNode = findNode(element);
        return lastNode != null && lastNode.isTerminal;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        size--;
        Node lastNode = removeTerminal(element);
        if (lastNode != null) {
            lastNode.isTerminal = false;
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Node lastNode = findNode(prefix);
        if (lastNode == null) {
            return 0;
        }
        return lastNode.terminalsNumber;
    }

    private Node findNode(String s) {
        return followPath(s, Function.identity());
    }

    private Node createPath(String s) {
        return followPath(s, (Node n) -> {
            if (n == null) {
                n = new Node();
            }
            n.terminalsNumber++;
            return n;
        });
    }

    private Node removeTerminal(String s) {
        return followPath(s, (Node n) -> {
            n.terminalsNumber--;
            if (n != root && n.terminalsNumber == 0) {
                return null;
            }
            return n;
        });
    }

    private Node followPath(String s, Function<Node, Node> f) {
        Node cur = f.apply(root);
        for (int i = 0; cur != null && i < s.length(); i++) {
            int index = getIndex(s.charAt(i));
            cur.children[index] = f.apply(cur.children[index]);
            cur = cur.children[index];
        }
        return cur;
    }

    private static int getIndex(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 'z' - 'a' + 1;
        }
        throw new AssertionError("Incorrect char: " + c);
    }

    private static class Node {
        private Node[] children = new Node[ALPHABET_SIZE];
        private boolean isTerminal = false;
        private int terminalsNumber = 0;
    }
}
