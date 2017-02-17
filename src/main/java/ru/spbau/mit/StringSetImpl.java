package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private static final int ALPHABET_SIZE = 52;

    private int size = 0;
    private Node root = new Node();

    @Override
    public boolean add(String element) {
        boolean res = add(root, element, 0);
        if (res) {
            size++;
        }
        return res;
    }

    private boolean add(Node cur, String s, int idx) {
        if (idx == s.length()) {
            if (cur.isTerminal) {
                return false;
            }
            cur.terminalsNumber++;
            cur.isTerminal = true;
            return true;
        }
        int childIdx = charToIdx(s.charAt(idx));
        Node next = cur.child[childIdx];
        if (next == null) {
            cur.child[childIdx] = new Node();
            next = cur.child[childIdx];
        }
        boolean res = add(next, s, idx + 1);
        if (res) {
            cur.terminalsNumber++;
        }
        return res;
    }

    @Override
    public boolean contains(String element) {
        return contains(root, element, 0);
    }

    private boolean contains(Node cur, String s, int idx) {
        if (idx == s.length()) {
            return cur.isTerminal;
        }
        int childIdx = charToIdx(s.charAt(idx));
        Node next = cur.child[childIdx];

        //noinspection SimplifiableIfStatement
        if (next == null || next.terminalsNumber == 0) {
            return false;
        }
        return contains(next, s, idx + 1);
    }

    @Override
    public boolean remove(String element) {
        return remove(root, element, 0);
    }

    private boolean remove(Node cur, String s, int idx) {
        if (idx == s.length()) {
            if (cur.isTerminal) {
                cur.isTerminal = false;
                size--;
                cur.terminalsNumber--;
                return true;
            }
            return false;
        }

        int childIdx = charToIdx(s.charAt(idx));
        Node next = cur.child[childIdx];
        if (next == null) {
            return false;
        }
        boolean res = remove(next, s, idx + 1);
        if (res) {
            cur.terminalsNumber--;
        }
        return res;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return howManyStartsWithPrefix(root, prefix, 0);
    }

    private int howManyStartsWithPrefix(Node cur, String s, int idx) {
        if (idx == s.length()) {
            return cur.terminalsNumber;
        }

        int childIdx = charToIdx(s.charAt(idx));
        Node next = cur.child[childIdx];
        if (next == null || next.terminalsNumber == 0) {
            return 0;
        }

        return howManyStartsWithPrefix(next, s, idx + 1);
    }

    private int charToIdx(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 'z' - 'a' + 1;
        }
        throw new AssertionError("Incorrect char: " + c);
    }

    private static class Node {
        private Node[] child = new Node[ALPHABET_SIZE];
        private boolean isTerminal = false;
        private int terminalsNumber = 0;
    }
}
