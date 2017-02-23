package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private int countWords;
    private Trie head;

    public StringSetImpl() {
        head = new Trie();
        countWords = 0;
    }
    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        Trie node = head;
        for (int i = 0; i < element.length(); i++) {
            int index = index(element.charAt(i));
            if (node.getTrie(index) == null) {
                node.setTrie(new Trie(), index);
            }
            node = node.getTrie(index);
            node.incHowManyStartsWithPrefix();
        }
        node.setTerminate(true);
        countWords++;
        return true;
    }

    @Override
    public boolean contains(String element) {
        Trie node = head;
        for (int i = 0; i < element.length(); i++) {
            node = node.getTrie(index(element.charAt(i)));
            if (node == null) {
                return false;
            }
        }
        return node.getTerminate();
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        Trie node = head;
        for (int i = 0; i < element.length(); i++) {
            node = node.getTrie(index(element.charAt(i)));
            if (node == null) {
                return false;
            }
            node.decHowManyStartsWithPrefix();
        }
        node.setTerminate(false);
        countWords--;
        return true;
    }

    @Override
    public int size() {
        return countWords;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Trie node = head;
        for (int i = 0; i < prefix.length(); i++) {
            if (node == null) {
                return 0;
            }
            node = node.getTrie(index(prefix.charAt(i)));
        }
        return prefix.length() == 0 ? countWords : node.getHowManyStartsWithPrefix();
    }

    private int index(char symbol) {
        final int start = 26;
        if (symbol >= 'a' && symbol <= 'z') {
            return start + (int) symbol - (int) 'a';
        }
        return (int) symbol - (int) 'A';
    }
}

class Trie {
    private boolean isTerminated;
    private final int size = 52;
    private int howManyStartsWithPrefix;
    private Trie[] listRef;

    Trie() {
        listRef = new Trie[size];
        howManyStartsWithPrefix = 0;
        isTerminated = false;
    }
    public boolean getTerminate() {
        return isTerminated;
    }

    public void setTerminate(boolean isTerminated) {
        this.isTerminated = isTerminated;
    }

    public int getHowManyStartsWithPrefix() {
        return howManyStartsWithPrefix;
    }

    public void incHowManyStartsWithPrefix() {
        howManyStartsWithPrefix++;
    }

    public void decHowManyStartsWithPrefix() {
        howManyStartsWithPrefix--;
    }

    public Trie getTrie(int index) {
        return listRef[index];
    }

    public void setTrie(Trie node, int index) {
        listRef[index] = node;
    }

}
