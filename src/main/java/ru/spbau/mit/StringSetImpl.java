package ru.spbau.mit;

/**
 * Created by yorov on 21.02.17.
 */
public class StringSetImpl implements StringSet {

    private static final int ALPHABET_SIZE = 26;
    private static final int CHAR_COUNT = 2 * ALPHABET_SIZE;

    private static final class Vertex {
        private Vertex[] next = new Vertex[CHAR_COUNT];
        private boolean isLeaf = false;
        private int count = 0;
    }

    private Vertex root;
    private int size;

    public StringSetImpl() {
        root = new Vertex();
        size = 0;
    }

    private static int index(char i) {
        if ('a' <= i && i <= 'z') {
            return ALPHABET_SIZE + (i - 'a');
        }
        return i - 'A';
    }

    private static boolean checkLeaf(Vertex node) {
        for (int i = 0; i < CHAR_COUNT; i++) {
            if (node.next[i] != null) {
                return false;
            }
        }
        return true;
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
        if (temp == null) {
            return false;
        }
        return temp.isLeaf;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        Vertex temp = root;
        for (int i = 0; i < element.length(); i++) {
            int ind = index(element.charAt(i));
            temp.count--;
            temp = temp.next[ind];
        }

        if (checkLeaf(temp)) {
            temp = null;
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
        if (temp == null) {
            return 0;
        }
        return temp.count;
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
