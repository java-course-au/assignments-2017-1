package ru.spbau.mit;


import java.util.ArrayList;


public class StringSetImpl implements StringSet {
    private Vertex root;
    private ArrayList<Vertex> data;

    public StringSetImpl() {
        data = new ArrayList<>();
        data.add(new Vertex());
        data.get(0).makeRoot();
        root = data.get(0);
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
            predVertex.makeEnd();
            for (int j = element.length() - 1; j > i; j--) {
                data.add(0, predVertex);
                predVertex = new Vertex();
                predVertex.setChild(element.charAt(j), data.get(0));
            }
            data.add(0, predVertex);
            currVertex.setChild(element.charAt(i), data.get(0));
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
        if (!curVertex.isEnd()) {
            return false;
        }
        return true;
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
        if (element == "") {
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
                data.remove(curChild);
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

