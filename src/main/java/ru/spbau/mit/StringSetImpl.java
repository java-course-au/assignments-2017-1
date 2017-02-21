package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private final Node root;

    public StringSetImpl() {
        root = new Node();
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        } else {
            Node curNode = root;
            for (int i = 0; i < element.length(); i++) {
                curNode.increaseNumStrings();
                if (curNode.getNext(element.charAt(i)) == null) {
                    curNode = curNode.setNext(element.charAt(i));
                } else {
                    curNode = curNode.getNext(element.charAt(i));
                }
            }
            curNode.increaseNumStrings();
            curNode.setIsEndOfString(true);
            return true;
        }
    }

    @Override
    public boolean contains(String element) {
        Node curNode = root;
        for (int i = 0; i < element.length(); i++) {
            if (curNode.getNext(element.charAt(i)) == null) {
                return false;
            } else {
                curNode = curNode.getNext(element.charAt(i));
            }
        }
        return curNode.isEndOfString();
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        } else {
            Node curNode = root;
            for (int i = 0; i < element.length() - 1; i++) {
                curNode.decreaseNumStrings();
                curNode = curNode.getNext(element.charAt(i));
            }
            curNode.decreaseNumStrings();
            curNode.setIsEndOfString(false);
            return true;
        }
    }

    @Override
    public int size() {
        return root.getNumStrings();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        if (!contains(prefix)) {
            return 0;
        } else {
            Node curNode = root;
            for (int i = 0; i < prefix.length(); i++) {
                curNode = curNode.getNext(prefix.charAt(i));
            }
            return curNode.getNumStrings();
        }
    }
}

class Node {
    private static final int NUMBER_OF_LETTERS = 52;
    private Node[] nodes;
    private boolean isEndOfString;
    private int numStrings;

    Node() {
        nodes = new Node[NUMBER_OF_LETTERS];
        isEndOfString = false;
        numStrings = 0;
    }

    private int idxFromChar(char chr) {
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
        return nodes[idxFromChar(chr)];
    }

    Node setNext(char chr) {
        Node newNode = new Node();
        nodes[idxFromChar(chr)] = newNode;
        return newNode;
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
