package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.Math.abs;

public class DictionaryImpl implements Dictionary {
    private static final int DEFAULT_BUCKETS_NUM = 1024;

    private LinkedList[] buckets = new LinkedList[DEFAULT_BUCKETS_NUM];
    private int capacity = 2 * buckets.length;
    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(@NotNull final String key) {
        return findNode(key) != null;
    }

    @Override
    @Nullable
    public String get(@NotNull final String key) {
        final LinkedList.Node node = findNode(key);
        return node != null ? node.getValue() : null;
    }

    @Override
    @Nullable
    public String put(@NotNull final String key,
                      @Nullable final String value) {
        final LinkedList.Node node = findNode(key);
        if (node != null) {
            final String oldValue = node.getValue();
            node.setValue(value);
            return oldValue;
        }

        final int bucketNum = getBucketNum(key);
        if (buckets[bucketNum] == null) {
            buckets[bucketNum] = new LinkedList();

        }

        final LinkedList.Node newNode = new LinkedList.Node(key, value);
        buckets[bucketNum].insert(newNode);
        size++;
        balanceLoadFactor();

        return null;
    }

    @Override
    @Nullable
    public String remove(@NotNull final String key) {
        final LinkedList.Node node = findNode(key);
        if (node == null) {
            return null;
        }

        final int bucketNum = getBucketNum(key);
        buckets[bucketNum].delete(key);
        size--;

        return node.getValue();
    }

    @Override
    public void clear() {
        buckets = new LinkedList[buckets.length];
        size = 0;
    }

    @Nullable
    private LinkedList.Node findNode(@NotNull final String key) {
        final LinkedList bucket = buckets[getBucketNum(key)];
        return bucket != null ? bucket.find(key) : null;
    }

    private int getBucketNum(@NotNull final String key) {
        return abs(key.hashCode() % buckets.length);
    }

    private void balanceLoadFactor() {
        if (size < capacity) {
            return;
        }

        final LinkedList[] oldBuckets = buckets;
        buckets = new LinkedList[capacity];
        capacity *= 2;
        size = 0;

        for (LinkedList bucket : oldBuckets) {
            for (LinkedList.Node node = bucket.getFirst();
                 node != null; node = node.getNext()) {
                //noinspection ConstantConditions
                put(node.getKey(), node.getValue());
            }
        }
    }
}
