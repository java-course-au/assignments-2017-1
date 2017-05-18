package ru.spbau.mit;

public abstract class Abstract implements Implementor, Cloneable {
    public abstract void foo(Integer arg);

    public int bar() {
        return 0;
    }
}
