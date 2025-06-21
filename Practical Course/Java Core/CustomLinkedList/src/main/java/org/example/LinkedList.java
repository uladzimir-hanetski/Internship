package org.example;

import java.util.NoSuchElementException;

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(T value) {
        if (head == null) {
            head = new Node<>(value, null, null);
            tail = head;
        } else {
            Node<T> newNode = new Node<>(value, head, null);
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T value) {
        if (tail == null) {
            head = new Node<>(value, null, null);
            tail = head;
        } else {
            Node<T> newNode = new Node<>(value, null, tail);
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public void add(int index, T value) {
        if (isIndexIncorrect(index)) throw new IndexOutOfBoundsException();

        if (index == 0) addFirst(value);
        else if (index == size) addLast(value);
        else {
            Node<T> temp = getNode(index);
            Node<T> newNode = new Node<>(value, temp, temp.prev);

            temp.prev.next = newNode;
            temp.prev = newNode;
            size++;
        }
    }

    public T getFirst() {
        if (head == null) throw new NoSuchElementException();

        return head.data;
    }

    public T getLast() {
        if (tail == null) throw new NoSuchElementException();

        return tail.data;
    }

    public T get(int index) {
        if (isIndexIncorrect(index)) throw new IndexOutOfBoundsException();

        return getNode(index).data;
    }

    public T removeFirst() {
        if (head == null) throw new NoSuchElementException();

        T data = head.data;
        if (head == tail) head = tail = null;
        else {
            head = head.next;
            head.prev = null;
        }
        size--;

        return data;
    }

    public T removeLast() {
        if (tail == null) throw new NoSuchElementException();

        T data = tail.data;
        if (tail == head) tail = head = null;
        else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;

        return data;
    }

    public T remove(int index) {
        if (isIndexIncorrect(index)) throw new IndexOutOfBoundsException();

        Node<T> temp = getNode(index);
        if (temp == head) return removeFirst();
        if (temp == tail) return removeLast();

        temp.prev.next = temp.next;
        temp.next.prev = temp.prev;
        size--;

        return temp.data;
    }

    private boolean isIndexIncorrect(int index) {
        return index < 0 || index > size;
    }

    private Node<T> getNode(int index) {
        Node<T> temp;
        if (index < size / 2) {
            temp = head;
            for (int i = 0; i < index; i++) temp = temp.next;
        } else {
            temp = tail;
            for (int i = size - 1; i > index; i--) temp = temp.prev;
        }

        return temp;
    }

    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        public Node(T data, Node<T> next, Node<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}