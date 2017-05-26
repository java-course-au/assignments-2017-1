package ru.spbau.mit;

import java.util.ArrayList;

public interface SuperInterface extends BaseInterface {

    Object objectMethod(String str);

    double doubleMethod();

    float floatMethod(int p, char c);

    ArrayList<String> arrayListMethod();

    char charMethod();

    boolean booleanMethod(int[] array);

    int[] simpleArrayMethod();

    String stringMethod();

    int varArgsMethod(String... strings);
}
