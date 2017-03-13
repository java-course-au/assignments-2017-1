package ru.spbau.mit;

public class StringSetImpl implements StringSet {

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
        final int asciiOffset = 65;
        return value - asciiOffset;
    }

    @Override
    public boolean remove(String element) {

        if (!contains(element)) {
            return false;
        }

        Node lastNode = findLastNode(element);
        lastNode.isFullWord = false;
        lastNode.howManyStartsWithPrefix--;

        char[] array = element.toCharArray();
        Node parent = lastNode.parent;

        // decrement value on path to root
        for (int i = element.length() - 1; i >= 0; i--) {
            parent.howManyStartsWithPrefix--;
            parent = parent.parent;
        }

        // delete unused prefix
        if (lastNode.howManyStartsWithPrefix == 0) {
            parent = lastNode.parent;
            for (int i = element.length() - 1; i >= 0; i--) {
                parent.children[getIndexByChar(array[i])] = null;
                if (parent.isFullWord || parent.howManyStartsWithPrefix > 0) {
                    break;
                }
                parent = parent.parent;
            }
        }

        return true;
    }

    @Override
    public int size() {
        return root.howManyStartsWithPrefix;
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

        private Node[] children;
        private char value;
        private int howManyStartsWithPrefix;
        private boolean isFullWord;
        private Node parent;


        Node(char value) {
            final int sizeOfFrameInAscii = 58;
            this.value = value;
            this.children = new Node[sizeOfFrameInAscii];
        }

        Node getChild(char value) {
            return children[getIndexByChar(value)];
        }

        void addChild(final Node newChild) {
            children[getIndexByChar(newChild.value)] = newChild;
        }
    }
}
