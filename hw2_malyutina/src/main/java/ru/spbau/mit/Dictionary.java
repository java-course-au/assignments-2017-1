package ru.spbau.mit;

public interface Dictionary {

    // хеш-таблица, использующая списоки
    // ключами и значениями выступают строки
    // стандартный способ получить хеш объекта -- вызвать у него метод hashCode()

    // кол-во ключей в таблице
    int size();

    // true, если такой ключ содержится в таблице
    boolean contains(String key);

    // возвращает значение, хранимое по ключу key
    // если такого нет, возвращает null
    String get(String key);

    // положить по ключу key значение value
    // и вернуть ранее хранимое, либо null
    String put(String key, String value);

    // забыть про пару key-value для переданного key
    // и вернуть забытое value, либо null, если такой пары не было
    String remove(String key);

    // забыть про все пары key-value
    void clear();
}

