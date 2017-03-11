package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {

    private static final double LOAD_FACTOR = 0.75;
    private static final int DOUBLED_CAPACITY = 2;
    private static final int DEFAULT_CAPACITY = 11;
    private static final class ListNode {
        private ListNode next = null;
        private String key;
        private String value;

        ListNode(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private int capacity = DEFAULT_CAPACITY;
    private int size = 0;
    private ListNode[] hashTable = new ListNode[capacity];

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
        ListNode temp = traverse(key);
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
        int hash = getBucketId(key);

        if (hashTable[hash] == null) {
            ++size;
            hashTable[hash] = new ListNode(key, value);
            return null;
        }
        ListNode cur = hashTable[hash];
        ListNode pred = null;

        while (cur != null && !key.equals(cur.key)) {
            pred = cur;
            cur = cur.next;
        }

        if (cur == null) {
            ++size;
            pred.next = new ListNode(key, value);
            return null;
        }

        String oldValue = cur.value;
        cur.value = value;
        return oldValue;

    }

    @Override
    public String remove(String key) {
        if (!contains(key)) {
            return null;
        }
        --size;
        int hash = getBucketId(key);
        ListNode cur = hashTable[hash];
        ListNode pred = cur;
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

    private static void clear(ListNode cur) {
        if (cur != null) {
            clear(cur.next);
            cur.next = null;
        }
    }

    private ListNode traverse(String key) {
        int hash = getBucketId(key);
        if (hashTable[hash] == null) {
            return null;
        }
        ListNode cur = hashTable[hash];
        while (cur != null && !key.equals(cur.key)) {
            cur = cur.next;
        }
        return cur;
    }

    private int getBucketId(String key) {
        int res = key.hashCode() % capacity;
        return res >= 0 ? res : (capacity + res);
    }

    private void rehash() {
        ListNode[] oldTable = hashTable;
        int oldCapacity = capacity;
        capacity = oldCapacity * DOUBLED_CAPACITY;
        hashTable = new ListNode[capacity];
        for (int i = 0; i < oldCapacity; ++i) {
            ListNode head = oldTable[i];
            while (head != null) {
                int hash = getBucketId(head.key);
                ListNode cur = hashTable[hash];
                if (cur == null) {
                    hashTable[hash] = new ListNode(head.key, head.value);
                } else {
                    while (cur.next != null) {
                        cur = cur.next;
                    }
                    cur.next = new ListNode(head.key, head.value);
                }
                ListNode pred = head;
                head = head.next;
                pred.next = null;
            }
            oldTable[i] = null;
        }
    }
}
