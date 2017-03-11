package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;

public class StringSetImpl implements StringSet, StreamSerializable {
    private static final int ALPHABET_SIZE = 52;

    private StringSetImpl[] children = new StringSetImpl[ALPHABET_SIZE];
    private boolean isTerminal;
    private int numTerminalsInSubset;

    @Override
    public boolean add(@NotNull final String element) {
        return add(element, 0);
    }

    @Override
    public boolean contains(@NotNull final String element) {
        return contains(element, 0);
    }

    @Override
    public boolean remove(@NotNull final String element) {
        return remove(element, 0);
    }

    @Override
    public int size() {
        return numTerminalsInSubset;
    }

    @Override
    public int howManyStartsWithPrefix(@NotNull final String prefix) {
        return howManyStartsWithPrefix(prefix, 0);
    }

    @Override
    public void serialize(@NotNull final OutputStream out) {
        final DataOutputStream dataOut = new DataOutputStream(out);
        try {
            dataOut.writeBoolean(isTerminal);
            dataOut.writeInt(numTerminalsInSubset);
            for (StringSetImpl child : children) {
                if (child == null) {
                    dataOut.writeBoolean(false);
                } else {
                    dataOut.writeBoolean(true);
                    child.serialize(out);
                }
            }
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void deserialize(@NotNull final InputStream in) {
        final DataInputStream dataIn = new DataInputStream(in);
        try {
            isTerminal = dataIn.readBoolean();
            numTerminalsInSubset = dataIn.readInt();
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                final boolean childExists = dataIn.readBoolean();
                if (childExists) {
                    children[i] = new StringSetImpl();
                    children[i].deserialize(in);
                }
            }
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final StringSetImpl another = (StringSetImpl) object;
        return isTerminal == another.isTerminal
                && numTerminalsInSubset == another.numTerminalsInSubset
                && Arrays.equals(children, another.children);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    private boolean add(@NotNull final String element, final int offset) {
        if (element.length() == offset) {
            if (!isTerminal) {
                isTerminal = true;
                numTerminalsInSubset++;
                return true;
            } else {
                return false;
            }
        }

        final int index = convertCharToIndex(element.charAt(offset));
        if (children[index] == null) {
            children[index] = new StringSetImpl();
        }

        final boolean result = children[index].add(element, offset + 1);
        if (result) {
            numTerminalsInSubset++;
        }

        return result;
    }

    private boolean contains(@NotNull final String element, final int offset) {
        if (element.length() == offset) {
            return isTerminal;
        }

        final int index = convertCharToIndex(element.charAt(offset));
        return children[index] != null
               && children[index].contains(element, offset + 1);
    }

    private boolean remove(@NotNull final String element, final int offset) {
        if (offset == element.length()) {
            if (isTerminal) {
                isTerminal = false;
                numTerminalsInSubset--;
                return true;
            } else {
                return false;
            }
        }

        final int index = convertCharToIndex(element.charAt(offset));
        if (children[index] == null) {
            return false;
        }

        final boolean result = children[index].remove(element, offset + 1);
        if (result) {
            numTerminalsInSubset--;
            if (children[index].numTerminalsInSubset == 0) {
                children[index] = null;
            }
        }

        return result;
    }

    private int howManyStartsWithPrefix(@NotNull final String prefix,
                                        final int offset) {
        if (offset == prefix.length()) {
            return numTerminalsInSubset;
        }

        final int index = convertCharToIndex(prefix.charAt(offset));
        if (children[index] == null) {
            return 0;
        }

        return children[index].howManyStartsWithPrefix(prefix, offset + 1);
    }

    private static int convertCharToIndex(final char ch) {
        if (Character.isLowerCase(ch)) {
            return ch - 'a';
        } else if (Character.isUpperCase(ch)) {
            return ch - 'A' + ALPHABET_SIZE / 2;
        } else {
            throw new IllegalArgumentException("Illegal character: " + ch);
        }
    }
}
