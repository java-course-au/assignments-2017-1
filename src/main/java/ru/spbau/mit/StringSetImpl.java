package ru.spbau.mit;

import java.io.*;

public class StringSetImpl implements StringSet, StreamSerializable {

    private static final int ALPHABET_SIZE = 26;
    private static final int CHAR_COUNT = 2 * ALPHABET_SIZE;

    private static final class Vertex {
        private Vertex[] next = new Vertex[CHAR_COUNT];
        private boolean isLeaf = false;
        private int prefixCount = 0;
        private short childCount = 0;
    }

    private Vertex root = new Vertex();
    private int size = 0;

    private static char indexToChar(int i) {
        if (0 <= i && i < ALPHABET_SIZE) {
            return (char) (i + 'A');
        }
        return (char) (i - ALPHABET_SIZE + 'a');
    }

    private static int index(char i) {
        if ('a' <= i && i <= 'z') {
            return ALPHABET_SIZE + (i - 'a');
        }
        return i - 'A';
    }

    @Override
    public void serialize(OutputStream out) {
        try (DataOutputStream ostream = new DataOutputStream(out)) {
            traverseAndSerialize(root, ostream);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private static void traverseAndSerialize(Vertex cur, DataOutputStream ostream)
        throws IOException {
        ostream.writeBoolean(cur.isLeaf);
        ostream.writeShort(cur.childCount);
        for (int i = 0; i < CHAR_COUNT; ++i) {
            if (cur.next[i] != null) {
                ostream.writeChar(indexToChar(i));
                traverseAndSerialize(cur.next[i], ostream);
            }
        }
    }

    @Override
    public void deserialize(InputStream in) {
        try (DataInputStream istream = new DataInputStream(in)) {
            root = new Vertex();
            size = traverseAndDeserialize(root, istream);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private static int traverseAndDeserialize(Vertex cur, DataInputStream istream)
        throws IOException {
        cur.isLeaf = istream.readBoolean();
        int prefixCount = cur.isLeaf ? 1 : 0;
        cur.childCount = istream.readShort();
        for (int i = 0; i < cur.childCount; ++i) {
            int ind = index(istream.readChar());
            cur.next[ind] = new Vertex();
            prefixCount += traverseAndDeserialize(cur.next[ind], istream);
        }
        cur.prefixCount = prefixCount;
        return prefixCount;
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        Vertex temp = root;
        for (int i = 0; i < element.length(); i++) {
            int ind = index(element.charAt(i));
            if (temp.next[ind] == null) {
                temp.childCount++;
                temp.next[ind] = new Vertex();
            }
            temp.prefixCount++;
            temp = temp.next[ind];
        }
        temp.isLeaf = true;
        temp.prefixCount++;
        size++;
        return true;
    }

    @Override
    public boolean contains(String element) {
        Vertex temp = traverse(element);
        return temp != null && temp.isLeaf;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        Vertex temp = root;
        Vertex pred = temp;
        int ind = 0;
        for (int i = 0; i < element.length(); i++) {
            ind = index(element.charAt(i));
            temp.prefixCount--;
            pred = temp;
            temp = temp.next[ind];
            if (pred.prefixCount == 0) {
                pred.childCount = 0;
                pred.next[ind] = null;
            }
        }

        if (temp.prefixCount == 1 && temp != pred) {
            pred.childCount--;
            pred.next[ind] = null;
        } else {
            temp.isLeaf = false;
            temp.prefixCount--;
        }
        size--;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Vertex temp = traverse(prefix);
        return (temp == null) ? 0 : temp.prefixCount;
    }

    private Vertex traverse(String str) {
        Vertex temp = root;
        for (int i = 0; i < str.length(); i++) {
            int ind = index(str.charAt(i));
            if (temp.next[ind] == null) {
                return null;
            }
            temp = temp.next[ind];
        }
        return temp;
    }

}
