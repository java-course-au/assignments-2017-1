package ru.spbau.mit;


/**
 * Created by katenos on 22.02.17.
 */
public class Vertex {
    private Vertex[] children;
    private Integer amountOfStrings;
    private boolean isRootVertex = false;
    private boolean isEndVertex = false;

    public Vertex() {
        children = new Vertex[2 * ((int) 'z' - (int) 'a' + 1)];
        amountOfStrings = 0;
    }

    private int getindex(char ch) {
        int ascii = (int) ch;
        if (((int) 'A') <= ascii && ascii <= ((int) 'Z')) {
            return (ascii - (int) 'A');
        } else {
            if ((int) 'a' <= ascii && ascii <= (int) 'z') {
                return (((int) 'z' - (2 * (int) 'a')) + ascii + 2);
            }
        }
        return 2 * ((int) 'z' - (int) 'a' + 1) + 1;
    }

    public void makeRoot() {
        isRootVertex = true;
    }

    public boolean isRoot() {
        return isRootVertex;
    }

    public void makeEnd() {
        isEndVertex = true;
        this.increaseAmountOfStrings();
    }

    public void loseEnd() {
        isEndVertex = false;
    }

    public boolean isEnd() {
        return isEndVertex;
    }

    public void setChild(Character ch, Vertex v) {
        children[getindex(ch)] = v;
        this.increaseAmountOfStrings();
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
        return (null != children[getindex(ch)]);
    }
}
