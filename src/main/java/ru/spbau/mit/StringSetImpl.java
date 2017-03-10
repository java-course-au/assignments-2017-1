package ru.spbau.mit;

import java.io.*;
import java.util.StringTokenizer;
import java.util.function.Function;

public class StringSetImpl implements StringSet, StreamSerializable {
    private static final int ALPHABET_SIZE = 52;

    private int size = 0;
    private Node root = new Node();

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        Node lastNode = createPath(element);
        lastNode.isTerminal = true;
        size++;
        return true;
    }

    @Override
    public boolean contains(String element) {
        Node lastNode = findNode(element);
        return lastNode != null && lastNode.isTerminal;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        size--;
        Node lastNode = removeTerminal(element);
        if (lastNode != null) {
            lastNode.isTerminal = false;
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Node lastNode = findNode(prefix);
        if (lastNode == null) {
            return 0;
        }
        return lastNode.terminalsNumber;
    }

    @Override
    public void serialize(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        traverse(root, pw);
        pw.close();
        if (pw.checkError()) {
            throw new SerializationException();
        }
    }

    private void traverse(Node n, PrintWriter pw) {
        pw.print(n.isTerminal ? 1 : 0);
        pw.print(" ");
        pw.println(n.terminalsNumber);
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (n.children[i] != null) {
                pw.print(i);
                pw.print(" ");
            }
        }
        pw.println();
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (n.children[i] != null) {
                traverse(n.children[i], pw);
            }
        }
    }

    @Override
    public void deserialize(InputStream in) {
        root = new Node();
        restore(root, new BufferedReader(new InputStreamReader(in)));
    }

    private void restore(Node cur, BufferedReader br) {
        StringTokenizer tokens = new StringTokenizer(safeRead(br));
        switch (safeNextToken(tokens)) {
            case "1":
                cur.isTerminal = true;
                break;
            case "0":
                cur.isTerminal = false;
                break;
            default:
                throw new SerializationException();
        }
        cur.terminalsNumber = safeParseInt(safeNextToken(tokens));

        tokens = new StringTokenizer(safeRead(br));
        while (tokens.hasMoreTokens()) {
            int idx = safeParseInt(tokens.nextToken());
            if (idx < 0 || idx >= ALPHABET_SIZE) {
                throw new SerializationException();
            }
            cur.children[idx] = new Node();
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (cur.children[i] != null) {
                restore(cur.children[i], br);
            }
        }
    }

    private String safeRead(BufferedReader br) {
        try {
            String s = br.readLine();
            if (s == null) {
                throw new SerializationException();
            }
            return s;
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    private String safeNextToken(StringTokenizer tokenizer) {
        if (!tokenizer.hasMoreTokens()) {
            throw new SerializationException();
        }
        return tokenizer.nextToken();
    }

    private int safeParseInt(String s) {
        if (s == null) {
            throw new SerializationException();
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new SerializationException();
        }
    }

    private Node findNode(String s) {
        return followPath(s, Function.identity());
    }

    private Node createPath(String s) {
        return followPath(s, (Node n) -> {
            if (n == null) {
                n = new Node();
            }
            n.terminalsNumber++;
            return n;
        });
    }

    private Node removeTerminal(String s) {
        return followPath(s, (Node n) -> {
            n.terminalsNumber--;
            if (n != root && n.terminalsNumber == 0) {
                return null;
            }
            return n;
        });
    }

    private Node followPath(String s, Function<Node, Node> f) {
        Node cur = f.apply(root);
        for (int i = 0; cur != null && i < s.length(); i++) {
            int index = getIndex(s.charAt(i));
            cur.children[index] = f.apply(cur.children[index]);
            cur = cur.children[index];
        }
        return cur;
    }

    private static int getIndex(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 'z' - 'a' + 1;
        }
        throw new AssertionError("Incorrect char: " + c);
    }

    private static class Node {
        private Node[] children = new Node[ALPHABET_SIZE];
        private boolean isTerminal = false;
        private int terminalsNumber = 0;
    }
}
