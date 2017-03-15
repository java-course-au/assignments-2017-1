package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StringSetImpl implements StringSet, StreamSerializable {
    private static class Trie {

        private boolean isTerminated = false;

        private int howManyStartsWithPrefix = 0;
        private Trie[] nodes = new Trie[SYMBOL_COUNT];
    }
    private static final int START_INDEX = 26;
    private static final int NULL_NODE_MARKER = 4;
    private static final int ROOT_MARKER = 3;
    private static final int TRUE_MARKER = 1;
    private static final int FALSE_MARKER = 2;
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

    @Override
    public void serialize(OutputStream out) {
        try {
            startSerialize(out, root);
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    private void startSerialize(OutputStream out, Trie root) throws IOException {
        if (root == this.root && root.howManyStartsWithPrefix == 0) {
            return;
        }
        if (root == this.root) {
            writeNode(out, root, ROOT_MARKER);
        }
        for (int i = 0; i < SYMBOL_COUNT; i++) {
            Trie node = root.nodes[i];
            if (node == null) {
                writeNode(out, null, NULL_NODE_MARKER);
            } else {
                writeNode(out, node, (int) getSymbol(i));
                startSerialize(out, node);
            }
        }
    }

    private static char getSymbol(int index) {
        if (index < START_INDEX) {
            return (char) (index + (int) 'A');
        }
        return (char) (index - START_INDEX + (int) 'a');
    }

    @Override
    public void deserialize(InputStream in) {
        try {
            clear();
            startDeserialize(in, root);
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    private void startDeserialize(InputStream in, Trie root) throws IOException {
        int iteration = 0;
        while (iteration < SYMBOL_COUNT) {
            int symbol = in.read();
            if (symbol == -1) {
                break;
            }
            if (symbol == NULL_NODE_MARKER) {
                iteration++;
                continue;
            } else if (symbol == ROOT_MARKER) {
                readNode(in, root);
                continue;

            } else {
                int index = getIndex((char) symbol);
                root.nodes[index] = new Trie();
                readNode(in, root.nodes[index]);
                startDeserialize(in, root.nodes[index]);
            }
            iteration++;
        }
    }

    private void readNode(InputStream in, Trie node) throws IOException {
        node.howManyStartsWithPrefix = in.read();
        node.isTerminated = in.read() == TRUE_MARKER;
    }

    private void writeNode(OutputStream out, Trie node, int symbol) throws IOException {
        out.write(symbol);
        if (node != null) {
            out.write(node.howManyStartsWithPrefix);
            out.write(node.isTerminated ? TRUE_MARKER : FALSE_MARKER);
        }
    }

    private void clear() {
        root = new Trie();
    }

}
