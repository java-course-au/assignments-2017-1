package ru.spbau.mit.test;

import java.io.Serializable;
import java.util.function.Function;

public interface SomeInterface extends AutoCloseable, Function, Serializable {
    int X = 1;

    void someMethod();
}
