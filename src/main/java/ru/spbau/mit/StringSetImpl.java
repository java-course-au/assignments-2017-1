package ru.spbau.mit;

import java.io.*;

public class StringSetImpl implements StringSet, StreamSerializable {
    private static class TrieNode {
        private static final int ALPHABET_SIZE = 52;
        private TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        private int stringsWithPref = 0;
        private boolean isWord = false;
    }

    private TrieNode root = new TrieNode();

    private static int charToIndex(char symbol) {
        if (symbol <= 'Z') {
            return symbol - 'A';
        } else {
            return symbol - 'a' + ('z' - 'a');
        }
    }

    private TrieNode find(String element) {
        int index = 0;
        TrieNode currentNode = root;

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

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        } else {
            TrieNode currentNode = root;
            int index = 0;

            while (index != element.length()) {
                currentNode.stringsWithPref++;
                int childIndex = charToIndex(element.charAt(index));
                TrieNode child = currentNode.children[childIndex];

                if (child == null) {
                    currentNode.children[childIndex] = new TrieNode();
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

    /**
     * Expected complexity: O(|element|)
     */
    @Override
    public boolean contains(String element) {
        TrieNode elementNode = find(element);
        return elementNode != null && elementNode.isWord;
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @return <tt>true</tt> if this set contained the specified element
     */
    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        } else {
            int index = 0;
            TrieNode currentNode = root;

            while (index != element.length()) {
                currentNode.stringsWithPref--;
                int childIndex = charToIndex(element.charAt(index));
                currentNode = currentNode.children[childIndex];
                index++;
            }
            currentNode.stringsWithPref--;
            currentNode.isWord = false;
            return true;
        }
    }

    /**
     * Expected complexity: O(1)
     */
    @Override
    public int size() {
        return root.stringsWithPref;
    }

    /**
     * Expected complexity: O(|prefix|)
     */
    @Override
    public int howManyStartsWithPrefix(String prefix) {
        TrieNode prefixNode = find(prefix);
        return prefixNode == null ? 0 : prefixNode.stringsWithPref;
    }

    private static void serializeNode(DataOutputStream dataOut, TrieNode node) throws IOException {
        dataOut.writeBoolean(node.isWord);

        for (int i = 0; i < TrieNode.ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                dataOut.writeInt(i);
                serializeNode(dataOut, node.children[i]);
            } else {
                dataOut.writeInt(-1);
            }
        }
    }

    @Override
    public void serialize(OutputStream out) {
        try (DataOutputStream dataOut = new DataOutputStream(out)) {
            serializeNode(dataOut, root);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private static int stringsWithPrefInChildren(TrieNode[] children) {
        int result = 0;
        for (TrieNode child : children) {
            if (child != null) {
                result += child.stringsWithPref;
            }
        }

        return result;
    }

    private static TrieNode[] deserializeChildren(DataInputStream dataIn) throws IOException {
        TrieNode[] children = new TrieNode[TrieNode.ALPHABET_SIZE];

        for (int i = 0; i < TrieNode.ALPHABET_SIZE; i++) {
            int index = dataIn.readInt();
            if (index != -1) {
                children[i] = new TrieNode();
                children[i].isWord = dataIn.readBoolean();
                children[i].children = deserializeChildren(dataIn);
                children[i].stringsWithPref = stringsWithPrefInChildren(children[i].children);
                if (children[i].isWord) {
                    children[i].stringsWithPref++;
                }
            }
        }
        return children;
    }

    @Override
    public void deserialize(InputStream in) {
        try (DataInputStream dataIn = new DataInputStream(in)) {
            root.isWord = dataIn.readBoolean();
            root.children = deserializeChildren(dataIn);
            root.stringsWithPref = stringsWithPrefInChildren(root.children);
            if (root.isWord) {
                root.stringsWithPref++;
            }
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
