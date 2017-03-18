package ru.spbau.mit;

import java.io.*;

public class StringSetImpl implements StringSet, StreamSerializable {

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

    @Override
    public void serialize(OutputStream out) {
        DataOutputStream str = new DataOutputStream(out);
        try {
            serializeRecursively(root, str);
        } catch (IOException ex) {
            throw new SerializationException();
        }

    }

    @Override
    public void deserialize(InputStream in) {
        DataInputStream str = new DataInputStream(in);
        try {
            root = deserializeRecursively(str);
        } catch (IOException ex) {
            throw new SerializationException();
        }
    }

    private void serializeRecursively(Node node, DataOutputStream str) throws IOException {
        writeNode(node, str);
        if (node.howManyStartsWithPrefix > 0) {
            str.writeBoolean(true);
            for (Node child : node.children) {
                if (child != null) {
                    serializeRecursively(child, str);
                }
            }
            final char noMoreChildren = 0;
            str.writeChar(noMoreChildren);
        } else {
            str.writeBoolean(false);
        }
    }

    private Node deserializeRecursively(DataInputStream str) throws IOException {
        Node node = readNode(str);
        if (node == null) {
            return null;
        }

        node.howManyStartsWithPrefix = node.isFullWord ? 1 : 0;

        boolean hasChildren = str.readBoolean();
        if (hasChildren) {
            Node child;
            while ((child = deserializeRecursively(str)) != null) {
                node.children[getIndexByChar(child.value)] = child;
                node.howManyStartsWithPrefix += child.howManyStartsWithPrefix;
                child.parent = node;
            }
        }
        return node;
    }

    private Node readNode(DataInputStream str) throws IOException {
        char value = str.readChar();
        if (value == 0) {
            return null;
        }

        Node node = new Node(value);
        node.isFullWord = str.readBoolean();

        return node;
    }

    private void writeNode(Node node, DataOutputStream str) throws IOException {
        str.writeChar(node.value);
        str.writeBoolean(node.isFullWord);
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
