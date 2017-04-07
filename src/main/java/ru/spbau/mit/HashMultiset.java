package ru.spbau.mit;

import java.util.*;

public class HashMultiset<T> extends AbstractCollection<T> implements Multiset<T> {
    private MyList<T> dataList = new MyList<>();
    private Map<T, NodeHandler<T>> dataMap = new HashMap<>();
    private int size = 0;
    private int modificationTime = 0;

    @Override
    public boolean add(T t) {
        if (!dataMap.containsKey(t)) {
            NodeHandler<T> nodeHandler = new NodeHandler<>(dataList.add(t));
            dataMap.put(t, nodeHandler);
        } else {
            dataMap.get(t).count++;
        }
        size++;
        modificationTime++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        NodeHandler<T> nodeHandler = dataMap.get(o);
        if (nodeHandler == null) {
            return false;
        }
        nodeHandler.count--;
        if (nodeHandler.count == 0) {
            dataMap.remove(o);
            dataList.remove(nodeHandler.node);
        }
        size--;
        modificationTime++;
        return true;
    }

    @Override
    public int count(Object element) {
        NodeHandler<T> nodeHandler = dataMap.get(element);
        return nodeHandler == null ? 0 : nodeHandler.count;
    }

    @Override
    public boolean contains(Object o) {
        return count(o) != 0;
    }

    @Override
    public Set<T> elementSet() {
        return new ElementSetView();
    }

    @Override
    public Set<? extends Entry<T>> entrySet() {
        return new EntrySetView();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Iterator<T> dataListIterator = dataList.iterator();
            private T currentValue = null;
            private int produced = 0;
            private int count = 0;
            private int modificationTime = HashMultiset.this.modificationTime;
            private boolean lastOperationIsNext = false;

            @Override
            public boolean hasNext() {
                checkForConcurrentModifications();
                return produced < count || dataListIterator.hasNext();
            }

            @Override
            public T next() {
                checkForConcurrentModifications();
                if (produced < count) {
                    produced++;
                    return currentValue;
                }
                currentValue = dataListIterator.next();
                count = dataMap.get(currentValue).count;
                produced = 1;
                lastOperationIsNext = true;
                return currentValue;
            }

            @Override
            public void remove() {
                checkForConcurrentModifications();
                if (!lastOperationIsNext) {
                    throw new NoSuchElementException();
                }
                modificationTime++;
                HashMultiset.this.modificationTime++;
                NodeHandler<T> nodeHandler = dataMap.get(currentValue);
                nodeHandler.count--;
                if (nodeHandler.count == 0) {
                    dataMap.remove(currentValue);
                    dataListIterator.remove();
                }
                HashMultiset.this.size--;
            }

            private void checkForConcurrentModifications() {
                if (modificationTime != HashMultiset.this.modificationTime) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    private static final class NodeHandler<T> {
        private MyList.Node<T> node;
        private int count;

        private NodeHandler(MyList.Node<T> node) {
            this.node = node;
            count = 1;
        }
    }

    private class ElementSetView extends AbstractSet<T> {
        private Iterator<T> dataListIterator = dataList.iterator();

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private int modificationTime = HashMultiset.this.modificationTime;

                @Override
                public boolean hasNext() {
                    checkForConcurrentModification();
                    return dataListIterator.hasNext();
                }

                @Override
                public T next() {
                    checkForConcurrentModification();
                    return dataListIterator.next();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                private void checkForConcurrentModification() {
                    if (modificationTime != HashMultiset.this.modificationTime) {
                        throw new ConcurrentModificationException();
                    }
                }
            };
        }

        @Override
        public int size() {
            return dataMap.size();
        }

        @Override
        public boolean contains(Object o) {
            return dataMap.containsKey(o);
        }
    }

    private class EntrySetView extends AbstractSet<Entry<T>> {
        private Iterator<T> dataListIterator = dataList.iterator();

        @Override
        public Iterator<Entry<T>> iterator() {
            return new Iterator<Entry<T>>() {
                private int modificationTime = HashMultiset.this.modificationTime;
                private Entry<T> lastEntry = null;

                @Override
                public boolean hasNext() {
                    checkForConcurrentModification();
                    return dataListIterator.hasNext();
                }

                @Override
                public Entry<T> next() {
                    checkForConcurrentModification();
                    lastEntry = new Entry<T>() {
                        private T value = dataListIterator.next();
                        private int count = dataMap.get(value).count;

                        @Override
                        public T getElement() {
                            return value;
                        }

                        @Override
                        public int getCount() {
                            return count;
                        }
                    };
                    return lastEntry;
                }

                @Override
                public void remove() {
                    checkForConcurrentModification();
                    if (lastEntry == null) {
                        throw new NoSuchElementException();
                    }
                    dataListIterator.remove();
                    dataMap.remove(lastEntry.getElement());
                    HashMultiset.this.modificationTime++;
                    modificationTime++;
                    HashMultiset.this.size -= lastEntry.getCount();
                    lastEntry = null;
                }

                private void checkForConcurrentModification() {
                    if (modificationTime != HashMultiset.this.modificationTime) {
                        throw new ConcurrentModificationException();
                    }
                }
            };
        }

        @Override
        public int size() {
            return dataMap.size();
        }
    }
}
