package ru.spbau.mit;

/**
 * Created by yorov on 21.02.17.
 */
public class StringSetImpl implements StringSet {

    static final int ALPHABET = 26;
    static final int CHCNTBWLOWANDUP = 6;
    static final int CHARCOUNT = 2 * ALPHABET;

    private static final class Vertex {
        private Vertex[] next;
        private boolean isLeaf;
        private int count;

        private Vertex() {
            next = new Vertex[CHARCOUNT];
            isLeaf = false;
            count = 0;
        }
    }

    private Vertex root;
    private int size;

    public StringSetImpl() {
        root = new Vertex();
        root.isLeaf = false;
        root.count = 0;
        size = 0;
    }

    private static int index(char i) {
        int temp = i - 'A';
        if (temp >= ALPHABET) {
            return temp - CHCNTBWLOWANDUP;
        }
        return temp;
    }

    @Override
    public boolean add(String element) {
        if (this.contains(element)) {
            return false;
        }

        Vertex temp = this.root;
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
        this.size++;
        return true;
    }

    @Override
    public boolean contains(String element) {
        Vertex temp = this.root;
        for (int i = 0; i < element.length(); i++) {
            int ind = index(element.charAt(i));
            if (temp.next[ind] == null) {
                return false;
            }
            temp = temp.next[ind];
        }

        return temp.isLeaf;
    }

    @Override
    public boolean remove(String element) {
        if (!this.contains(element)) {
            return false;
        }

        Vertex temp = this.root;
        for (int i = 0; i < element.length(); i++) {
            int ind = index(element.charAt(i));
            temp.count--;
            temp = temp.next[ind];

        }
        temp.isLeaf = false;
        this.size--;
        temp.count--;
        return true;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Vertex temp = this.root;
        for (int i = 0; i < prefix.length(); i++) {
            int ind = index(prefix.charAt(i));
            if (temp.next[ind] == null) {
                return 0;
            }
            temp = temp.next[ind];
        }
        return temp.count;
    }
}
