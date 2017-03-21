package ru.spbau.mit;

import java.io.*;

public class StringSetImpl implements StringSet, StreamSerializable {
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

    @Override
    public void serialize(OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            startSerialize(dos, root);
        } catch (IOException e) {
            throw new SerializationException();
        }
    }
    private void startSerialize(DataOutputStream dos, Trie root) throws IOException {
        dos.writeBoolean(root.isTerminated);
        int countNotNull = 0;
        for (Trie node : root.nodes) {
            if (node != null) {
                countNotNull++;
            }
        }
        dos.writeInt(countNotNull);
        for (int i = 0; i < SYMBOL_COUNT; i++) {
            if (root.nodes[i] != null) {
                dos.writeInt(i);
                startSerialize(dos, root.nodes[i]);
            }
        }
    }

    @Override
    public void deserialize(InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            clear();
            startDeserialize(dis, root);
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    private int startDeserialize(DataInputStream in, Trie root) throws IOException {
        root.isTerminated = in.readBoolean();
        if (root.isTerminated) {
            root.howManyStartsWithPrefix++;
        }
        int countNodes = in.readInt();
        for (int i = 0; i < countNodes; i++) {
            int index = in.readInt();
            root.nodes[index] = new Trie();
            root.howManyStartsWithPrefix += startDeserialize(in, root.nodes[index]);
        }
        return root.howManyStartsWithPrefix;
    }

    private void clear() {
        root = new Trie();
    }

}
