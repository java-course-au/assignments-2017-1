package ru.spbau.mit;

public abstract class SuperAbstract implements SuperInterface {
    @Override
    public int methodA() {
        return 0;
    }

    abstract protected void methodF();

    static void methodG() {

    }

    abstract public void methodC(int a);

    abstract public void methodE(String... a);
}
