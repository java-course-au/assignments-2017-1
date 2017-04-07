package ru.spbau.mit;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* package */ class MyList<T> implements Iterable<T> {
    private Node<T> head = new Node<>(null);
    private Node<T> tail = head;
    private int modificationTime = 0;

    Node<T> add(T value) {
        modificationTime++;

        Node<T> node = new Node<>(value);
        node.prev = tail;
        tail.next = node;
        tail = node;
        return node;
    }

    void remove(Node<T> node) {
        modificationTime++;
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        node.prev.next = node.next;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> curNode = head;
            private int modificationTime = MyList.this.modificationTime;

            @Override
            public boolean hasNext() {
                checkForConcurrentModifications();
                return curNode.next != null;
            }

            @Override
            public T next() {
                checkForConcurrentModifications();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                curNode = curNode.next;
                return curNode.value;
            }

            @Override
            public void remove() {
                checkForConcurrentModifications();
                if (curNode == head) {
                    throw new NoSuchElementException();
                }
                Node<T> prev = curNode.prev;
                MyList.this.remove(curNode);
                modificationTime++;
                curNode = prev;
            }

            private void checkForConcurrentModifications() {
                if (modificationTime != MyList.this.modificationTime) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    public static final class Node<T> {
        private T value;
        private Node<T> prev;
        private Node<T> next;

        private Node(T value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Node<?> node = (Node<?>) o;

            return value != null ? value.equals(node.value) : node.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }
}
