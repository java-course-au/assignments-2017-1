package ru.spbau.mit;

public class StringSetImpl implements StringSet {

    private static class Trie {
        private static final int ALPHABET_SIZE = 52;
        private Trie[] children;
        private int stringsWithPref = 0;
        private boolean isWord = false;

        Trie() {
            children = new Trie[ALPHABET_SIZE];
        }

        private int charToIndex(char symbol) {
            if (symbol <= 'z') {
                return symbol - 'A';
            } else return symbol - 'a' + ('z' - 'a');
        }

        boolean add(String element) {
            if (!contains(element)) {
                Trie currentNode = this;
                int index = 0;

                while (index != element.length()) {
                    currentNode.stringsWithPref++;
                    int childIndex = charToIndex(element.charAt(index));
                    Trie child = currentNode.children[childIndex];

                    if (child == null) {
                        currentNode.children[childIndex] = new Trie();
                        child = currentNode.children[childIndex];
                    }
                    currentNode = child;
                    index++;
                }
                currentNode.stringsWithPref++;
                currentNode.isWord = true;
            } else {
                return false;
            }

            return true;
        }

        boolean contains(String element) {
            int index = 0;
            Trie currentNode = this;

            while (index != element.length()) {
                int childIndex = charToIndex(element.charAt(index));
                Trie child = currentNode.children[childIndex];

                if (child == null) {
                    return false;
                }
                if (index == element.length() - 1) {
                    return child.isWord;
                }
                currentNode = child;
                index++;
            }
            return isWord;
        }

        boolean remove(String element) {
            if (contains(element)) {
                int index = 0;
                Trie currentNode = this;

                while (index != element.length()) {
                    currentNode.stringsWithPref--;
                    int childIndex = charToIndex(element.charAt(index));
                    currentNode = currentNode.children[childIndex];
                    index++;
                }
                currentNode.stringsWithPref--;
                currentNode.isWord = false;
            } else {
                return false;
            }
            return true;
        }

        int size() {
            return stringsWithPref;
        }

        int howManyStartsWithPrefix(String prefix) {
            int index = 0;
            Trie currentNode = this;

            while (index != prefix.length()) {
                int childIndex = charToIndex(prefix.charAt(index));
                currentNode = currentNode.children[childIndex];
                if (currentNode == null) {
                    return 0;
                }
                index++;
            }

            return currentNode.stringsWithPref;
        }
    }

    private Trie tree;

    public StringSetImpl() {
        tree = new Trie();
    }

    @Override
    public boolean add(String element) {
        return tree.add(element);
    }

    /**
     * Expected complexity: O(|element|)
     */
    @Override
    public boolean contains(String element) {
        return tree.contains(element);
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @return <tt>true</tt> if this set contained the specified element
     */
    @Override
    public boolean remove(String element) {
        return tree.remove(element);
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
        return tree.howManyStartsWithPrefix(prefix);
    }
}

