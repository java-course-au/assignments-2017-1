package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private static class Vertex {
        private static final int SIZE = 2 * ('z' - 'a' + 1);
        private Vertex[] children = new Vertex[SIZE];
        private int amountOfStrings = 0;
        private boolean isEndVertex = false;

        private static int getindex(char ch) {
            if ('A' <= ch && ch <= 'Z') {
                return (ch - 'A');
            } else {
                if ('a' <= ch && ch <= 'z') {
                    return ('z' - (2 * 'a') + ch + 1);
                }
            }
            return 2 * ('z' - 'a' + 1) + 1;
        }

        public void makeEnd() {
            isEndVertex = true;
            increaseAmountOfStrings();
        }

        public void loseEnd() {
            isEndVertex = false;
        }

        public boolean isEnd() {
            return isEndVertex;
        }

        public void setChild(Character ch, Vertex v) {
            children[getindex(ch)] = v;
            increaseAmountOfStrings();
        }

        public void deleteChild(char ch) {
            children[getindex(ch)] = null;
        }

        public void increaseAmountOfStrings() {
            amountOfStrings++;
        }

        public void decreaseAmountOfStrings() {
            amountOfStrings--;
        }

        public Integer getAmountOfStrings() {
            return amountOfStrings;
        }

        public Vertex getChild(Character ch) {
            return children[getindex(ch)];
        }

        public boolean contains(Character ch) {
            return null != children[getindex(ch)];
        }
    }
    private Vertex root;

    public StringSetImpl() {
        root = new Vertex();
    }

    /**
     * Expected complexity: O(|element|)
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element
     */
    @Override
    public boolean add(String element) {
        if (this.contains(element)) {
            return false;
        }
        Vertex currVertex =  root;
        int i;
        for (i = 0; i < element.length(); i++) {
            if (currVertex.contains(element.charAt(i))) {
                currVertex.increaseAmountOfStrings();
                currVertex = currVertex.getChild(element.charAt(i));
            } else {
                break;
            }
        }
        if (i == element.length()) {
            currVertex.makeEnd();
        } else {
            Vertex predVertex = new Vertex();
            Vertex childVertex;
            predVertex.makeEnd();
            for (int j = element.length() - 1; j > i; j--) {
                childVertex = predVertex;
                predVertex = new Vertex();
                predVertex.setChild(element.charAt(j), childVertex);
            }
            currVertex.setChild(element.charAt(i), predVertex);
        }
        return true;
    }


    /**
     * Expected complexity: O(|element|)
     */
    @Override
    public  boolean contains(String element) {
        Vertex curVertex = root;
        for (int i = 0; i < element.length(); i++) {
            if (!curVertex.contains(element.charAt(i))) {
                return false;
            }
            curVertex = curVertex.getChild(element.charAt(i));
        }
        return curVertex.isEnd();
    }

    /**
     * Expected complexity: O(|element|)
     * @return <tt>true</tt> if this set contained the specified element
     */
    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        if (element.equals("")) {
            root.loseEnd();
            return true;
        }
        Vertex curVertex = root;
        Vertex curChild = root.getChild(element.charAt(0));
        root.decreaseAmountOfStrings();
        for (int i = 0; i < element.length(); i++) {
            curChild.decreaseAmountOfStrings();
            if (curChild.getAmountOfStrings() == 0) {
                curVertex.deleteChild(element.charAt(i));
            }
            if (i != element.length() - 1) {
                curVertex = curChild;
                curChild = curChild.getChild(element.charAt(i + 1));
            } else {
                curChild.loseEnd();
            }
        }
        return true;
    }

    /**
     * Expected complexity: O(1)
     */
    @Override
    public int size() {
        return root.getAmountOfStrings();
    }

    /**
     * Expected complexity: O(|prefix|)
     */
    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Vertex curVertex = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (!curVertex.contains(prefix.charAt(i))) {
                return 0;
            }
            curVertex = curVertex.getChild(prefix.charAt(i));
        }
        return curVertex.getAmountOfStrings();
    }
}

