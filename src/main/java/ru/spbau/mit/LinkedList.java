package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class LinkedList {
    @NotNull
    private Node head = new Node(null, null);

    @Nullable
    Node find(@NotNull final String key) {
        for (Node it = head.nextNode;
             it != null; it = it.nextNode) {
            if (key.equals(it.getKey())) {
                return it;
            }
        }

        return null;
    }

    void insert(@NotNull final Node node) {
        node.nextNode = head.nextNode;
        head.nextNode = node;
    }

    String delete(@NotNull final String key) {
        for (Node it = head, itNext = head.nextNode;
             itNext != null;
             it = itNext, itNext = itNext.nextNode) {
            if (key.equals(itNext.getKey())) {
                it.nextNode = itNext.nextNode;
                return itNext.getValue();
            }
        }

        return null;
    }

    @Nullable
    Node getFirst() {
        return head.getNext();
    }

    static class Node {
        @Nullable
        private final String key;
        @Nullable
        private String value;
        @Nullable
        private Node nextNode;

        Node(@Nullable final String key,
                     @Nullable final String value) {
            this.key = key;
            this.value = value;
        }


        @Nullable
        String getKey() {
            return key;
        }

        @Nullable
        String getValue() {
            return value;
        }

        void setValue(@Nullable final String value) {
            this.value = value;
        }

        @Nullable
        Node getNext() {
            return nextNode;
        }
    }
}
