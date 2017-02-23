package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private int countWords = 0;
    private final int size = 52;
    private Trie[] head = new Trie[size];
//    StringSetImpl() {
//        countWords = 0;
//        head = new Trie[size];
//    }

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
            if (index == -1 || node[index] == null) {
                return false;
            }
            if (i == element.length() - 1) {
                return node[index].getTerminate();
            }
            node = node[index].getListRef();
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
            int howMany = node[index].getHowManyStartsWithPrefix();
            node[index].setHowManyStartsWithPrefix(++howMany);
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
            if (index == -1 || node[index] == null) {
                break;
            }
            if (i == prefix.length() - 1) {
                return node[index].getHowManyStartsWithPrefix();
            }
            node = node[index].getListRef();
        }
        return 0;
    }

    private int index(char symbol) {
        final int start = 27;
        if (symbol >= 'a' && symbol <= 'z') {
            return start + (int) symbol - (int) 'a';
        } else if (symbol >= 'A' && symbol <= 'Z') {
            return (int) symbol - (int) 'A';
        }
        return -1;
    }
}

class Trie {
    private boolean isTerminated;
    private final int size = 52;
    private int howManyStartsWithPrefix;
    private Trie[] listRef;

    Trie() {
        isTerminated = false;
        howManyStartsWithPrefix = 0;
        listRef = new Trie[size];
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

    public void setHowManyStartsWithPrefix(int howManyStartsWithPrefix) {
        this.howManyStartsWithPrefix = howManyStartsWithPrefix;
    }

    public Trie[] getListRef() {
        return listRef;
    }
}
