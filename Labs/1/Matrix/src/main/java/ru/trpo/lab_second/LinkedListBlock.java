package ru.trpo.lab_second;

public class LinkedListBlock {

    private static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public int size() {
        return size;
    }

    public void addLast(int value) {
        Node n = new Node(value);
        if (head == null) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public int get(int index) {
        checkIndex(index);
        Node n = head;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n.value;
    }

    public void insert(int index, int value) {
        if (index == size) {
            addLast(value);
            return;
        }
        checkIndex(index);

        Node n = new Node(value);
        if (index == 0) {
            n.next = head;
            head = n;
            if (tail == null) tail = n;
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            n.next = prev.next;
            prev.next = n;
        }
        size++;
    }

    public void remove(int index) {
        checkIndex(index);

        if (index == 0) {
            head = head.next;
            if (head == null) tail = null;
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            if (prev.next == tail) {
                tail = prev;
            }
            prev.next = prev.next.next;
        }
        size--;
    }

    public void forEach(VectorOfLists.IntVisitor v) {
        Node n = head;
        while (n != null) {
            v.toDo(n.value);
            n = n.next;
        }
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public void lexicographicalSort() {
        if (size <= 1) return;

        head = mergeSort(head);

        tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }
    }

    private Node mergeSort(Node h) {
        if (h == null || h.next == null) {
            return h;
        }

        Node middle = getMiddle(h);
        Node nextOfMiddle = middle.next;
        middle.next = null;

        Node left = mergeSort(h);
        Node right = mergeSort(nextOfMiddle);

        return merge(left, right);
    }

    private Node merge(Node a, Node b) {
        if (a == null) return b;
        if (b == null) return a;

        String sa = String.valueOf(a.value);
        String sb = String.valueOf(b.value);

        if (sa.compareTo(sb) <= 0) {
            a.next = merge(a.next, b);
            return a;
        } else {
            b.next = merge(a, b.next);
            return b;
        }
    }

    private Node getMiddle(Node h) {
        Node slow = h;
        Node fast = h.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }
}