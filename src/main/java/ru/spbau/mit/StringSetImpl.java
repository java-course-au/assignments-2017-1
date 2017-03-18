package ru.spbau.mit;

import java.io.*;

public class StringSetImpl implements StringSet, StreamSerializable {

    private static final int LETTER_COUNT = 'Z' - 'A' + 'z' - 'a' + 2;

    private Node head;

    public StringSetImpl() {
        head = new Node();
    }

    public void serialize(OutputStream out) throws SerializationException {
        final DataOutputStream stream = new DataOutputStream(out);
        try {
            serializeNode(head, stream);
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    public void deserialize(InputStream in) throws SerializationException {
        final DataInputStream stream = new DataInputStream(in);
        final Node newHead = new Node();
        try {
            deserializeNode(newHead, stream);
            // strong exception safety (commit or rollback semantics)
            head = newHead;
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    public boolean add(String element) {
        final Trace trace = search(element);
        Node cur = trace.node;

        if (trace.foundTerminal()) {
            return false;
        }
        for (int i = trace.length; i < element.length(); i++) {
            cur = cur.addSymbol(element.charAt(i));
        }
        setTerminal(element);
        return true;
    }

    public boolean contains(String element) {
        final Trace trace = search(element);
        return trace.foundTerminal();
    }

    public boolean remove(String element) {
        final Trace res = search(element);
        if (!res.foundTerminal()) {
            return false;
        }
        ceaseTerminal(element);
        reclaimTail(element);
        return true;
    }

    public int size() {
        return head.size;
    }

    public int howManyStartsWithPrefix(String prefix) {
        final Trace trace = search(prefix);
        if (!trace.found()) {
            return 0;
        }
        return trace.node.size;
    }

    private void serializeNode(final Node node, final DataOutputStream out) throws IOException {
        int transitionCount = node.getTransitionCount();
        out.writeInt(transitionCount);
        out.writeBoolean(node.isTerminal);

        for (int i = 0; i < node.nexts.length; i++) {
            final Node next = node.nexts[i];
            if (next != null) {
                out.writeInt(i);
                serializeNode(next, out);
            }
        }
    }

    private void deserializeNode(final Node node, final DataInputStream in)
            throws IOException, SerializationException {
        final int transitionCount = in.readInt();
        node.isTerminal = in.readBoolean();
        int terminalCount = node.isTerminal ? 1 : 0;

        for (int i = 0; i < transitionCount; i++) {
            final int transitionSymbol = in.readInt();
            if (transitionSymbol < 0 || transitionSymbol >= LETTER_COUNT) {
                throw new SerializationException();
            }

            final Node next = new Node();
            deserializeNode(next, in);
            node.nexts[transitionSymbol] = next;
            terminalCount += next.size;
        }
        node.size = terminalCount;
    }

    private Trace search(String element) {
        Node cur = head;
        for (int i = 0; i < element.length(); i++) {
            Node next = cur.searchSymbol(element.charAt(i));
            if (next == null) {
                return new Trace(cur, i, element);
            }
            cur = next;
        }
        return new Trace(cur, element.length(), element);
    }

    private Node changeTraceSize(String element, int delta) {
        Node cur = head;
        for (int i = 0; i < element.length(); i++) {
            cur.size += delta;
            cur = cur.searchSymbol(element.charAt(i));
        }
        cur.size += delta;
        return cur;
    }

    private void setTerminal(String element) {
        Node tail = changeTraceSize(element, 1);
        tail.isTerminal = true;
    }

    private void ceaseTerminal(String element) {
        Node tail = changeTraceSize(element, -1);
        tail.isTerminal = false;
    }

    private void reclaimTail(String element) {
        Node cur = head;
        for (int i = 0; i < element.length(); i++) {
            Node next = cur.searchSymbol(element.charAt(i));
            if (next.size == 0) {
                cur.remove(element.charAt(i));
                return;
            }
            cur = next;
        }
    }

    private static final class Node {
        private boolean isTerminal;
        private int size;
        private Node[] nexts;

        Node() {
            isTerminal = false;
            nexts = new Node[LETTER_COUNT];
            size = 0;
        }

        int charToIndex(char ch) {
            if (Character.isLowerCase(ch)) {
                return ch - 'a';
            } else {
                return ch - 'A' + 'z' - 'a' + 1;
            }
        }

        Node searchSymbol(char symbol) {
            return nexts[charToIndex(symbol)];
        }

        Node addSymbol(char symbol) {
            Node next = new Node();
            nexts[charToIndex(symbol)] = next;
            return next;
        }
        void remove(char symbol) {
            nexts[charToIndex(symbol)] = null;
        }

        private int getTransitionCount() {
            int count = 0;
            for (Node next: nexts) {
                if (next != null) {
                    count += 1;
                }
            }
            return count;
        }
    }

    private static final class Trace {
        private Node node;
        private int length;
        private String element;

        Trace(Node node, int length, String element) {
            this.node = node;
            this.length = length;
            this.element = element;
        }

        boolean found() {
            return length == element.length();
        }
        boolean foundTerminal() {
            return found() && node.isTerminal;
        }
    }
}
