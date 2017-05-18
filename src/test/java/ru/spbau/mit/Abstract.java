package ru.spbau.mit;

public abstract class Abstract implements Implementor, Cloneable {
    public abstract void foo(Integer arg);

    protected int protFunc() {
        return 0;
    }

    public int bar() {
        return 0;
    }
}
