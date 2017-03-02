package ru.spbau.mit;

public class StringSetImpl implements StringSet {

    private static class Trie {
        private static final int ALPHABET_SIZE = 52;
        private Trie[] children = new Trie[ALPHABET_SIZE];
        private int stringsWithPref = 0;
        private boolean isWord = false;

        private int charToIndex(char symbol) {
            if (symbol <= 'Z') {
                return symbol - 'A';
            } else {
                return symbol - 'a' + ('z' - 'a');
            }
        }

        private Trie find(String element) {
            int index = 0;
            Trie currentNode = this;

            while (index != element.length()) {
                int childIndex = charToIndex(element.charAt(index));
                currentNode = currentNode.children[childIndex];
                if (currentNode == null) {
                    return null;
                }
                index++;
            }

            return currentNode;
        }

        boolean add(String element) {
            if (contains(element)) {
                return false;
            } else {
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
                return true;
            }
        }

        boolean contains(String element) {
            Trie elementNode = find(element);
            return elementNode != null && elementNode.isWord;
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
                return true;
            } else {
                return false;
            }
        }

        int size() {
            return stringsWithPref;
        }

        int howManyStartsWithPrefix(String prefix) {
            Trie prefixNode = find(prefix);
            return prefixNode == null ? 0 : prefixNode.stringsWithPref;
        }
    }

    private Trie trie = new Trie();

    @Override
    public boolean add(String element) {
        return trie.add(element);
    }

    /**
     * Expected complexity: O(|element|)
     */
    @Override
    public boolean contains(String element) {
        return trie.contains(element);
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @return <tt>true</tt> if this set contained the specified element
     */
    @Override
    public boolean remove(String element) {
        return trie.remove(element);
    }

    /**
     * Expected complexity: O(1)
     */
    @Override
    public int size() {
        return trie.size();
    }

    /**
     * Expected complexity: O(|prefix|)
     */
    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return trie.howManyStartsWithPrefix(prefix);
    }
}
