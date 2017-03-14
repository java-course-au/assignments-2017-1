package ru.spbau.mit;

import java.io.*;

public class StringSetImpl implements StringSet, StreamSerializable {
    private final Node root;

    public StringSetImpl() {
        root = new Node();
    }

    private Node find(String element) {
        Node curNode = root;
        for (int i = 0; i < element.length(); i++) {
            curNode = curNode.getNext(element.charAt(i));
            if (curNode == null) {
                return null;
            }
        }
        return curNode;
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        Node curNode = root;
        for (int i = 0; i < element.length(); i++) {
            curNode.increaseNumStrings();
            char curChar = element.charAt(i);
            if (curNode.getNext(curChar) == null) {
                curNode = curNode.setNext(curChar);
            } else {
                curNode = curNode.getNext(curChar);
            }
        }

        curNode.increaseNumStrings();
        curNode.setIsEndOfString(true);
        return true;
    }

    @Override
    public boolean contains(String element) {
        Node node = find(element);
        return node != null && node.isEndOfString();
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        Node curNode = root;
        for (int i = 0; i < element.length(); i++) {
            curNode.decreaseNumStrings();
            Node prevNode = curNode;
            char curChar = element.charAt(i);
            curNode = curNode.getNext(curChar);
            if (curNode.getNumStrings() == 1) {
                prevNode.delete(curChar);
            }
        }
        curNode.decreaseNumStrings();
        curNode.setIsEndOfString(false);
        return true;
    }

    @Override
    public int size() {
        return root.getNumStrings();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Node node = find(prefix);
        return node == null ? 0 : node.getNumStrings();
    }

    @Override
    public void serialize(OutputStream out) {
        root.serialize(out);
    }

    @Override
    public void deserialize(InputStream in) {
        root.clear();
        root.deserialize(in);
    }

    private static class Node implements StreamSerializable {
        private static final int NUMBER_OF_LETTERS = 52;
        private Node[] nodes = new Node[NUMBER_OF_LETTERS];
        private boolean isEndOfString = false;
        private int numStrings = 0;
        private int numBusyChars = 0;

        private static int getIndexFromChar(char chr) {
            int ichr = (int) chr;
            final int a = 65;
            final int z = 90;
            final int bigA = 97;
            if (a <= ichr && ichr <= z) {
                return ichr - a;
            } else { // (A <= ichr <= Z)
                return ichr - bigA + NUMBER_OF_LETTERS / 2;
            }
        }

        private Node getNext(char chr) {
            return nodes[getIndexFromChar(chr)];
        }

        private Node setNext(char chr) {
            Node newNode = new Node();
            if (nodes[getIndexFromChar(chr)] == null) {
                numBusyChars++;
            }
            nodes[getIndexFromChar(chr)] = newNode;
            return newNode;
        }

        private void delete(char chr) {
            nodes[getIndexFromChar(chr)] = null;
            numBusyChars--;
        }

        private void clear() {
            for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
                nodes[i] = null;
            }
            numBusyChars = 0;
        }

        private boolean isEndOfString() {
            return isEndOfString;
        }

        private void setIsEndOfString(boolean value) {
            isEndOfString = value;
        }

        private int getNumStrings() {
            return numStrings;
        }

        private void increaseNumStrings() {
            numStrings++;
        }

        private void decreaseNumStrings() {
            numStrings--;
        }

        private void doSerialize(OutputStream out) throws IOException {
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            dataOutputStream.writeInt(numBusyChars);
            dataOutputStream.writeBoolean(isEndOfString);

            for (int i = 0; i < NUMBER_OF_LETTERS; i++) {
                final Node node = nodes[i];
                if (node != null) {
                    dataOutputStream.writeInt(i);
                    node.doSerialize(out);
                }
            }
        }

        private int doDeserialize(InputStream in) throws IOException {
            DataInputStream dataInputStream = new DataInputStream(in);
            numBusyChars = dataInputStream.readInt();
            isEndOfString = dataInputStream.readBoolean();

            if (numBusyChars == 0 && isEndOfString) {
                numStrings = 1;
                return 1;
            }

            if (isEndOfString) {
                numStrings = 1;
            } else {
                numStrings = 0;
            }
            for (int i = 0; i < numBusyChars; i++) {
                int charPosition = dataInputStream.readInt();
                nodes[charPosition] = new Node();
                numStrings += nodes[charPosition].doDeserialize(in);
            }

            return numStrings;
        }

        public void serialize(OutputStream out) {
            try {
                doSerialize(out);
            } catch (IOException e) {
                throw new SerializationException();
            }
        }

        public void deserialize(InputStream in) {
            try {
                doDeserialize(in);
            } catch (IOException e) {
                throw new SerializationException();
            }
        }
    }
}
