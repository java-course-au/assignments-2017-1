package ru.spbau.mit;

import java.util.ArrayList;

public class StringSetImpl implements StringSet {
    private Trie tree;

    public StringSetImpl() {
        tree = new Trie();
    }

    @Override
    public boolean add(String element) {
        return tree.add(element, 0);
    }

    /**
     * Expected complexity: O(|element|)
     */
    @Override
    public boolean contains(String element) {
        return tree.contains(element, 0);
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @return <tt>true</tt> if this set contained the specified element
     */
    @Override
    public boolean remove(String element) {
        return tree.remove(element, 0);
    }

    /**
     * Expected complexity: O(1)
     */
    @Override
    public int size() {
        return tree.size();
    }

    /**
     * Expected complexity: O(|prefix|)
     */
    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return tree.howManyStartsWithPrefix(prefix, 0);
    }
}


class Trie {
    private ArrayList<Trie> children;
    private int stringsWithPref = 0;
    private char symbol;
    private boolean isWord = false;

    Trie() {
        children = new ArrayList<>();
    }

    private Trie(char symbol) {
        this.symbol = symbol;
        children = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Trie)) {
            return false;
        }
        Trie other = (Trie) obj;
        return other.symbol == symbol;
    }

    @Override
    public int hashCode() {
        return new Character(symbol).hashCode();
    }

    boolean add(String element, int index) {
        if (index == element.length()) {
            stringsWithPref++;
            isWord = true;
            return true;
        }
        int next = children.indexOf(new Trie(element.charAt(index)));
        if ((index == element.length() - 1) && (next != -1)
                && (children.get(next).isWord)) {
            return false;
        }
        if (next == -1) {
            children.add(new Trie(element.charAt(index)));
            next = children.indexOf(new Trie(element.charAt(index)));
        }
        boolean added = children.get(next).add(element, index + 1);
        if (added) {
            stringsWithPref++;
        }
        return added;
    }

    boolean contains(String element, int index) {
        if (element.length() == 0) {
            return isWord;
        }
        int next = children.indexOf(new Trie(element.charAt(index)));
        if (next == -1) {
            return false;
        }
        if (index == element.length() - 1) {
            return children.get(next).isWord;
        }
        return children.get(next).contains(element, index + 1);
    }

    boolean remove(String element, int index) {
        if (element.length() == 0) {
            if (isWord) {
                isWord = false;
                stringsWithPref--;
                return true;
            }
            return false;
        }
        int next = children.indexOf(new Trie(element.charAt(index)));
        if (next == -1) {
            return false;
        }
        if (index == element.length() - 1) {
            if ((children.get(next).isWord)) {
                if (children.get(next).stringsWithPref == 1) {
                    children.remove(next);
                } else {
                    children.get(next).isWord = false;
                    children.get(next).stringsWithPref--;
                }
                stringsWithPref--;
                return true;
            } else {
                return false;
            }
        }
        boolean deleted = children.get(next).remove(element, index + 1);
        if (deleted) {
            if (children.get(next).stringsWithPref == 0) {
                children.remove(next);
            }
            stringsWithPref--;
        }
        return deleted;
    }

    int size() {
        return stringsWithPref;
    }

    int howManyStartsWithPrefix(String prefix, int index) {
        if (index == prefix.length()) {
            return stringsWithPref;
        }
        int next = children.indexOf(new Trie(prefix.charAt(index)));
        if (next == -1) {
            return 0;
        }
        return children.get(next).howManyStartsWithPrefix(prefix, index + 1);
    }
}
