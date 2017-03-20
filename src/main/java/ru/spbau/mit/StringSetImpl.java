package ru.spbau.mit;

import java.io.*;
import java.util.function.Function;

public class StringSetImpl implements StringSet, StreamSerializable {
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

    @Override
    public void serialize(OutputStream out) {
        try (DataOutputStream data = new DataOutputStream(out)) {
            data.writeInt(size);
            traverse(root, data);
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    private void traverse(Node n, DataOutputStream data) throws IOException {
        data.writeBoolean(n.isTerminal);
        data.writeInt(n.terminalsNumber);
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (n.children[i] != null) {
                data.writeInt(i);
            }
        }
        data.writeInt(-1);
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (n.children[i] != null) {
                traverse(n.children[i], data);
            }
        }
    }

    @Override
    public void deserialize(InputStream in) {
        try (DataInputStream data = new DataInputStream(in)) {
            size = data.readInt();
            root = new Node();
            restore(root, data);
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    private void restore(Node cur, DataInputStream data) throws IOException {
        cur.isTerminal = data.readBoolean();
        cur.terminalsNumber = data.readInt();

        int idx;
        while ((idx = data.readInt()) != -1) {
            cur.children[idx] = new Node();
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (cur.children[i] != null) {
                restore(cur.children[i], data);
            }
        }
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
