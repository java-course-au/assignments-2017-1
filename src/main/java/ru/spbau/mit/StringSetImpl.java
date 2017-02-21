package ru.spbau.mit;

public class StringSetImpl implements StringSet {

    private static final int LETTER_COUNT = 'Z' - 'A' + 'z' - 'a' + 2;

    final class Node {
        private boolean isTerminal;
        private int size;
        private Node[] nexts;

        Node() {
            isTerminal = false;
            nexts = new Node[LETTER_COUNT];
            size = 0;
        }

        int charToIndex(char ch) {
            if (Character.isLowerCase(ch)) {
                return ch - 'a';
            } else {
                return ch - 'A' + 'z' - 'a' + 1;
            }
        }

        Node searchSymbol(char symbol) {
            return nexts[charToIndex(symbol)];
        }

        Node addSymbol(char symbol) {
            Node next = new Node();
            nexts[charToIndex(symbol)] = next;
            return next;
        }
        void remove(char symbol) {
            nexts[charToIndex(symbol)] = null;
        }
    }

    final class Trail {
        private Node node;
        private int elementCnt;

        Trail(Node nd, int cnt) {
            node = nd;
            elementCnt = cnt;
        }

        boolean match(String prefix) {
            return elementCnt == prefix.length();
        }
        boolean equal(String element) {
            return match(element) && node.isTerminal;
        }
    }

    private Node head;

    public StringSetImpl() {
        head = new Node();
    }

    private Trail search(String element) {
        Node cur = head;

        for (int i = 0; i < element.length(); i++) {
            Node next = cur.searchSymbol(element.charAt(i));
            if (next == null) {
                return new Trail(cur, i);
            }
            cur = next;
        }
        return new Trail(cur, element.length());
    }

    public boolean add(String element) {
        Trail trail = search(element);
        Node cur = trail.node;

        if (trail.equal(element)) {
            return false;
        }

        for (int i = trail.elementCnt; i < element.length(); i++) {
            cur = cur.addSymbol(element.charAt(i));
        }
        cur.isTerminal = true;

        cur = head;
        for (int i = 0; i < element.length(); i++) {
            cur.size++;
            cur = cur.searchSymbol(element.charAt(i));
        }
        cur.size++;
        return true;
    }

    public boolean contains(String element) {
        Trail trail = search(element);
        return trail.equal(element);
    }

    public boolean remove(String element) {
        Trail res = search(element);
        if (!res.equal(element)) {
            return false;
        }

        Node cur = head;
        for (int i = 0; i < element.length(); i++) {
            cur.size--;
            cur = cur.searchSymbol(element.charAt(i));
        }
        cur.size--;
        cur.isTerminal = false;

        cur = head;
        for (int i = 0; i < element.length(); i++) {
            Node next = cur.searchSymbol(element.charAt(i));
            if (next.size == 0) {
                cur.remove(element.charAt(i));
                break;
            }
            cur = next;
        }
        return true;
    }

    public int size() {
        return head.size;
    }

    public int howManyStartsWithPrefix(String prefix) {
        Trail trail = search(prefix);
        if (!trail.match(prefix)) {
            return 0;
        }
        return trail.node.size;
    }
}
