package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private int countWords = 0;
    private final int size = 52;
    private Trie[] head = new Trie[size];

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        Trie[] node = head;
        int index = 0;
        for (int i = 0; i < element.length(); i++) {
            index = index(element.charAt(i));
            if (node[index] == null) {
                node[index] = new Trie();
            }
            int howMany = node[index].getHowManyStartsWithPrefix();
            node[index].setHowManyStartsWithPrefix(++howMany);
            if (i == element.length() - 1) {
                node[index].setTerminate(true);
                countWords++;
            }
            node = node[index].getListRef();
        }
        return !(element.length() == 0);
    }

    @Override
    public boolean contains(String element) {
        Trie[] node = head;
        int index = 0;
        for (int i = 0; i < element.length(); i++) {
            index = index(element.charAt(i));
            if (node[index] == null) {
                break;
            }
            if (i == element.length() - 1) {
                return node[index].getTerminate();
            }
            node = node[index].getListRef();
        }
        return false;
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
            int howMany = node[index].getHowManyStartsWithPrefix();
            node[index].setHowManyStartsWithPrefix(--howMany);
            if (i == element.length() - 1) {
                node[index].setTerminate(false);
            }
            node = node[index].getListRef();
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
            if (node[index] == null) {
                return 0;
            }
            if (i == prefix.length() - 1) {
                return node[index].getHowManyStartsWithPrefix();
            }
            node = node[index].getListRef();
        }
        return countWords;
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
    private boolean isTerminated = false;
    private final int size = 52;
    private int howManyStartsWithPrefix = 0;
    private Trie[] listRef = new Trie[size];

    public boolean getTerminate() {
        return isTerminated;
    }

    public void setTerminate(boolean isTerminated) {
        this.isTerminated = isTerminated;
    }

    public int getHowManyStartsWithPrefix() {
        return howManyStartsWithPrefix;
    }

    public void setHowManyStartsWithPrefix(int howManyStartsWithPrefix) {
        this.howManyStartsWithPrefix = howManyStartsWithPrefix;
    }

    public Trie[] getListRef() {
        return listRef;
    }
}
