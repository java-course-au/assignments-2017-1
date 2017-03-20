package ru.spbau.mit;

import java.io.*;

public class StringSetImpl implements StringSet, StreamSerializable {

    private static final int ALPHABET_SIZE = 26;
    private static final int CHAR_COUNT = 2 * ALPHABET_SIZE;
    private static final char IS_LEAF = '!';
    private static final char END_OF_BRANCH = ')';

    private static final class Vertex {
        private Vertex[] next = new Vertex[CHAR_COUNT];
        private boolean isLeaf = false;
        private int count = 0;
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
        if (cur.isLeaf) {
            ostream.writeChar(IS_LEAF);
        }
        for (int i = 0; i < CHAR_COUNT; ++i) {
            if (cur.next[i] != null) {
                ostream.writeChar(indexToChar(i));
                traverseAndSerialize(cur.next[i], ostream);
            }
        }
        ostream.writeChar(END_OF_BRANCH);
    }

    @Override
    public void deserialize(InputStream in) {
        try (DataInputStream istream = new DataInputStream(in)) {
            root = new Vertex();
            traverseAndDeserialize(root, istream);
            size = root.count;
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private static void traverseAndDeserialize(Vertex cur, DataInputStream istream)
        throws IOException {

        if (istream.available() == 0) {
            return;
        }
        char c = istream.readChar();
        if (c == END_OF_BRANCH) {
            return;
        }
        if (c == IS_LEAF) {
            cur.isLeaf = true;
            cur.count = 1;
            c = istream.readChar();
            if (c == END_OF_BRANCH) {
                return;
            }
        }
        int ind = index(c);
        cur.next[ind] = new Vertex();
        traverseAndDeserialize(cur.next[ind], istream);
        cur.count += cur.next[ind].count;
        traverseAndDeserialize(cur, istream);

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
                temp.next[ind] = new Vertex();
            }
            temp.count++;
            temp = temp.next[ind];
        }
        temp.isLeaf = true;
        temp.count++;
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
            temp.count--;
            pred = temp;
            temp = temp.next[ind];
            if (pred.count == 0) {
                pred.next[ind] = null;
            }
        }

        if (temp.count == 1 && temp != pred) {
            pred.next[ind] = null;
        } else {
            temp.isLeaf = false;
            temp.count--;
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
        return (temp == null) ? 0 : temp.count;
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
