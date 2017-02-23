package ru.spbau.mit;

public class StringSetImpl implements StringSet {

    private int size;
    private Node root;

    StringSetImpl() {
        root = new Node('a');
    }

    @Override
    public boolean add(String element) {

        if (contains(element)) {
            return false;
        }

        Node node = root;
        for (char value : element.toCharArray()) {
            Node child = node.getChild(value);
            if (child == null) {
                child = new Node(value);
                child.parent = node;
                node.addChild(child);

            }
            node.howManyStartsWithPrefix++;
            node = child;
        }

        node.isFullWord = true;
        node.howManyStartsWithPrefix++;
        size++;

        return true;

    }

    private Node findLastNode(String element) {

        Node node = root;
        for (char value : element.toCharArray()) {
            Node child = node.getChild(value);
            if (child == null) {
                return null;
            }
            node = child;
        }

        return node;

    }

    @Override
    public boolean contains(String element) {

        Node node = findLastNode(element);

        return node != null && node.isFullWord;
    }

    private int getIndexByChar(char value) {
        final int ASCII_OFFSET = 65;
        return value - ASCII_OFFSET;
    }

    @Override
    public boolean remove(String element) {

        if (!contains(element)) {
            return false;
        }

        Node lastNode = findLastNode(element);
        lastNode.isFullWord = false;
        lastNode.howManyStartsWithPrefix--;
        size--;

        char[] array = element.toCharArray();
        Node parent = lastNode.parent;

        // decrement value on path to root
        for (int i = element.length() - 1; i >= 0; i--) {
            parent.howManyStartsWithPrefix--;
            parent = parent.parent;
        }

        // delete unused prefix
        if (lastNode.numberOfChildren == 0) {
            parent = lastNode.parent;
            for (int i = element.length() - 1; i >= 0; i--) {
                parent.numberOfChildren--;
                parent.children[getIndexByChar(array[i])] = null;
                if (parent.isFullWord || parent.numberOfChildren > 0) {
                    break;
                }
                parent = parent.parent;
            }
        }

        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Node lastNode = findLastNode(prefix);
        if (lastNode == null) {
            return 0;
        }
        return lastNode.howManyStartsWithPrefix;
    }

    class Node {

        Node[] children;
        char value;
        int numberOfChildren;
        int howManyStartsWithPrefix;
        boolean isFullWord;
        Node parent;


        Node(char value) {
            this.value = value;
            this.children = new Node[58];
        }

        Node getChild(char value) {
            return children[getIndexByChar(value)];
        }

        void addChild(Node newChild) {
            children[getIndexByChar(newChild.value)] = newChild;
            numberOfChildren++;
        }
    }
}