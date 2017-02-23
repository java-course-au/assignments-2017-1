package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private Trie[] head;
    private int countWords;

    StringSetImpl() {
        countWords = 0;
        head = new Trie[52];
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        Trie[] node = head;
        int index = 0;
        for (int i = 0; i < element.length(); i++) {
            index = index(element.charAt(i));
            if (index == -1) {
                return false;
            }
            if (node[index] == null) {
                node[index] = new Trie();
            }
            node[index].howManyStartsWithPrefix++;
            if (i == element.length() - 1) {
                node[index].isTerminated = true;
                countWords++;
            }
            node = node[index].listRef;
        }
        return !(element.length() == 0);
    }

    @Override
    public boolean contains(String element) {
        Trie[] node = head;
        int index = 0;
        for (int i = 0; i < element.length(); i++) {
            index = index(element.charAt(i));
            if (index == -1 || node[index] == null) {
                return false;
            }
            if (i == element.length() - 1) {
                return node[index].isTerminated;
            }
            node = node[index].listRef;
        }
        return !(element.length() == 0);
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        Trie[] node = head;
        int index = 0;
        for (int i = 0; i < element.length(); i++) {
            index = index(element.charAt(i));
            if (index == -1) {
                return false;
            }
            node[index].howManyStartsWithPrefix--;
            if (i == element.length() - 1) {
                node[index].isTerminated = false;
            }
            node = node[index].listRef;
        }
        countWords--;
        return true;
    }

    @Override
    public int size() {
        return countWords;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        int index = 0;
        Trie[] node = head;
        for (int i = 0; i < prefix.length(); i++) {
            index = index(prefix.charAt(i));
            if (index == -1 || node[index] == null) {
                break;
            }
            if (i == prefix.length() - 1) {
                return node[index].howManyStartsWithPrefix;
            }
            node = node[index].listRef;
        }
        return 0;
    }

    static private int index(char symbol) {
        if (symbol >= 'a' && symbol <= 'z') {
            return 27 + (int) symbol - (int) 'a';
        } else if (symbol >= 'A' && symbol <= 'Z') {
            return (int) symbol - (int) 'A';
        }
        return -1;
    }
}

class Trie {
    boolean isTerminated;
    int howManyStartsWithPrefix;
    Trie[] listRef;

    Trie() {
        isTerminated = false;
        howManyStartsWithPrefix = 0;
        listRef = new Trie[52];
    }
}
