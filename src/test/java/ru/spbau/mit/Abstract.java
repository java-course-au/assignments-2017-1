package ru.spbau.mit;

public abstract class Abstract extends Abstract2 implements Implementor {
    public abstract void foo(Integer arg);

    protected int protFunc() {
        return 0;
    }

    public int bar() {
        return 0;
    }
}
