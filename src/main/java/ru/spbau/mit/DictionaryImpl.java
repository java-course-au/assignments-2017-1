package ru.spbau.mit;

public class DictionaryImpl implements Dictionary {
    private static final int START_SIZE = 32;
    private static final double LOAD_FACTOR_UP = 0.75;
    private static final double LOAD_FACTOR_DOWN = 0.15;
    private int sizeMap = START_SIZE;
    private int countElementsUp = (int) (START_SIZE * LOAD_FACTOR_UP);
    private int countElementsDown = (int) (START_SIZE * LOAD_FACTOR_DOWN);
    private int countElements = 0;
    private ListBucket[] hashMap = new ListBucket[START_SIZE];

    @Override
    public int size() {
        return countElements;
    }
    @Override
    public boolean contains(String key) {
        int hash = getBucketID(key, sizeMap);
        return hashMap[hash] != null && hashMap[hash].contains(key);
    }

    @Override
    public String get(String key) {
        int bucketID = getBucketID(key, sizeMap);
        if (hashMap[bucketID] == null) {
            return null;
        }
        return hashMap[bucketID].getValue(key);
    }

    @Override
    public String put(String key, String value) {
        int bucketID = getBucketID(key, sizeMap);
        if (hashMap[bucketID] == null) {
            hashMap[bucketID] = new ListBucket();
        } else if (hashMap[bucketID].contains(key)) {
            String out = hashMap[bucketID].getValue(key);
            hashMap[bucketID].replace(key, value);
            return out;
        }
        hashMap[bucketID].add(key, value);
        countElements++;
        if (countElements > countElementsUp) {
            rehash(sizeMap * 2);
        }
        return null;
    }

    @Override
    public String remove(String key) {
        int bucketID = getBucketID(key, sizeMap);
        if (hashMap[bucketID] == null || !hashMap[bucketID].contains(key)) {
            return null;
        }
        countElements--;
        if (countElements < countElementsDown && sizeMap > START_SIZE) {
            rehash(sizeMap / 2);
        }
        return hashMap[bucketID].remove(key);
    }

    @Override
    public void clear() {
        hashMap = new ListBucket[sizeMap];
        countElements = 0;
    }

    private void rehash(int newSizeMap) {
        ListBucket[] newHashMap = new ListBucket[newSizeMap];
        for (int i = 0; i < sizeMap; i++) {
            if (hashMap[i] != null) {
                for (String key : hashMap[i].getKeys()) {
                    int bucketID = getBucketID(key, newSizeMap);
                    if (newHashMap[bucketID] == null) {
                        newHashMap[bucketID] = new ListBucket();
                    }
                    newHashMap[bucketID].add(key, hashMap[i].getValue(key));
                }
            }
        }
        hashMap = newHashMap;
        sizeMap = newSizeMap;
        countElementsUp = (int) (sizeMap * LOAD_FACTOR_UP);
        countElementsDown = (int) (sizeMap * LOAD_FACTOR_DOWN);
    }

    private int getBucketID(String key, int val) {
        return Math.abs(key.hashCode()) % val;
    }

    private static class ListBucket {
        private Node headBucket;
        private Node tailBucket;
        private int count;

        private Node getNode(String key) {
            Node currentNode = headBucket;
            while (currentNode != null) {
                if (currentNode.key.equals(key)) {
                    return currentNode;
                }
                currentNode = currentNode.next;
            }
            return null;
        }
        public void add(String key, String value) {
            if (headBucket == null) {
                headBucket = new Node(key, value);
                tailBucket = headBucket;
            } else {
                tailBucket.next = new Node(key, value);
                tailBucket.next.prev = tailBucket;
                tailBucket = tailBucket.next;
            }
            count++;
        }

        public boolean replace(String key, String newValue) {
            Node node = getNode(key);
            if (node != null) {
                node.value = newValue;
                return true;
            }
            return false;
        }
        public String remove(String key) {
            Node node = getNode(key);
            if (node != null) {
                if (node.next == null && node.prev == null) {
                    headBucket = null;
                    tailBucket = null;
                } else if (node.next == null) {
                    tailBucket = tailBucket.prev;
                    tailBucket.next = null;
                } else if (node.prev == null) {
                    headBucket = headBucket.next;
                    headBucket.prev = null;
                } else {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                }
                count--;
                return node.value;
            }
            return null;
        }

        public String getValue(String key) {
            Node node = getNode(key);
            return node != null ? node.value : null;
        }

        public boolean contains(String key) {
            Node node = getNode(key);
            return node != null;
        }

        public String[] getKeys() {
            if (headBucket == null) {
                return null;
            }
            String[] out = new String[count];
            int currentIndex = 0;
            Node currentNode = headBucket;
            while (currentNode != null) {
                out[currentIndex] = currentNode.key;
                currentNode = currentNode.next;
                currentIndex++;
            }
            return out;
        }
        private static class Node {
            private String key;
            private String value;
            private Node next;
            private Node prev;
            Node(String key, String value) {
                this.key = key;
                this.value = value;
            }
        }

    }
}
