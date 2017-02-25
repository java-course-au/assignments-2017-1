package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private final Node root;

    public StringSetImpl() {
        root = new Node();
    }

    private Node find(String element) {
        Node curNode = root;
        for (int i = 0; i < element.length(); i++) {
            curNode = curNode.getNext(element.charAt(i));
            if (curNode == null) {
                return null;
            }
        }
        return curNode;
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        Node curNode = root;
        for (int i = 0; i < element.length(); i++) {
            curNode.increaseNumStrings();
            char curChar = element.charAt(i);
            if (curNode.getNext(curChar) == null) {
                curNode = curNode.setNext(curChar);
            } else {
                curNode = curNode.getNext(curChar);
            }
        }

        curNode.increaseNumStrings();
        curNode.setIsEndOfString(true);
        return true;
    }

    @Override
    public boolean contains(String element) {
        Node node = find(element);
        return node != null && node.isEndOfString();
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        Node curNode = root;
        for (int i = 0; i < element.length(); i++) {
            curNode.decreaseNumStrings();
            Node prevNode = curNode;
            char curChar = element.charAt(i);
            curNode = curNode.getNext(curChar);
            if (curNode.getNumStrings() == 0) {
                prevNode.delete(curChar);
            }
        }
        curNode.decreaseNumStrings();
        curNode.setIsEndOfString(false);
        return true;
    }

    @Override
    public int size() {
        return root.getNumStrings();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Node node = find(prefix);
        return node == null ? 0 : node.getNumStrings();
    }

    private static class Node {
        private static final int NUMBER_OF_LETTERS = 52;
        private Node[] nodes = new Node[NUMBER_OF_LETTERS];
        private boolean isEndOfString = false;
        private int numStrings = 0;

        private static int getIndexFromChar(char chr) {
            int ichr = (int) chr;
            final int a = 65;
            final int z = 90;
            final int bigA = 97;
            if (a <= ichr && ichr <= z) {
                return ichr - a;
            } else { // (A <= ichr <= Z)
                return ichr - bigA + NUMBER_OF_LETTERS / 2;
            }
        }

        Node getNext(char chr) {
            return nodes[getIndexFromChar(chr)];
        }

        Node setNext(char chr) {
            Node newNode = new Node();
            nodes[getIndexFromChar(chr)] = newNode;
            return newNode;
        }

        void delete(char chr) {
            nodes[getIndexFromChar(chr)] = null;
        }

        boolean isEndOfString() {
            return isEndOfString;
        }

        void setIsEndOfString(boolean value) {
            isEndOfString = value;
        }

        int getNumStrings() {
            return numStrings;
        }

        void increaseNumStrings() {
            numStrings++;
        }

        void decreaseNumStrings() {
            numStrings--;
        }
    }
}
