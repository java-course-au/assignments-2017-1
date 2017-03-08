package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {

    private static final double LOAD_FACTOR = 0.75;
    private static final int DOUBLED_CAPACITY = 2;
    private static final int DEFAULT_CAPACITY = 11;
    private static final class List {
        private List next = null;
        private String key;
        private String value;

        List(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private int capacity = DEFAULT_CAPACITY;
    private int size = 0;
    private List[] hashTable = new List[capacity];

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(String key) {
        return traverse(key) != null;
    }

    @Override
    public String get(String key) {
        List temp = traverse(key);
        if (temp == null) {
            return null;
        }
        return temp.value;
    }

    @Override
    public String put(String key, String value) {
        if ((double) size / capacity >= LOAD_FACTOR) {
            rehash();
        }
        int hash = getHashCode(key);
        List cur = hashTable[hash];
        List pred = cur;
        String oldValue = null;

        if (contains(key)) {
            while (!key.equals(cur.key)) {
                pred = cur;
                cur = cur.next;
            }
            oldValue = cur.value;
            cur.value = value;
            if (pred == cur) {
                hashTable[hash] = cur;
            } else {
                pred.next = cur;
            }
        } else {
            ++size;
            while (cur != null) {
                pred = cur;
                cur = cur.next;
            }
            if (hashTable[hash] == null) {
                hashTable[hash] = new List(key, value);
            } else {
                pred.next = new List(key, value);
            }
        }
        return oldValue;
    }

    @Override
    public String remove(String key) {
        if (!contains(key)) {
            return null;
        }
        --size;
        int hash = getHashCode(key);
        List cur = hashTable[hash];
        List pred = cur;
        String value;

        while (!key.equals(cur.key)) {
            pred = cur;
            cur = cur.next;
        }
        value = cur.value;
        if (pred == cur) {
            hashTable[hash] = cur.next;
        } else {
            pred.next = cur.next;
        }
        return value;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; ++i) {
            clear(hashTable[i]);
            hashTable[i] = null;
        }
        size = 0;
    }

    private static void clear(List cur) {
        if (cur != null) {
            clear(cur.next);
            cur.next = null;
        }
    }

    private List traverse(String key) {
        int hash = getHashCode(key);
        if (hashTable[hash] == null) {
            return null;
        }
        List cur = hashTable[hash];
        while (cur != null && !key.equals(cur.key)) {
            cur = cur.next;
        }
        return cur;
    }

    private int getHashCode(String key) {
        int res = key.hashCode() % capacity;
        return res >= 0 ? res : (capacity + res);
    }

    private void rehash() {
        List[] oldTable = hashTable;
        int oldCapacity = capacity;
        capacity = oldCapacity * DOUBLED_CAPACITY;
        hashTable = new List[capacity];
        for (int i = 0; i < oldCapacity; ++i) {
            List head = oldTable[i];
            while (head != null) {
                int hash = getHashCode(head.key);
                List cur = hashTable[hash];
                if (cur == null) {
                    hashTable[hash] = new List(head.key, head.value);
                } else {
                    while (cur.next != null) {
                        cur = cur.next;
                    }
                    cur.next = new List(head.key, head.value);
                }
                List pred = head;
                head = head.next;
                pred.next = null;
            }
            oldTable[i] = null;
        }
    }
}
