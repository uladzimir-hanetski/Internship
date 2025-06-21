package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinkedListTest {
    private LinkedList<Integer> list;

    @BeforeEach
    void initialize() {
        list = new LinkedList<>();
    }

    @Test
    void testEmptyList() {
        assertEquals(0, list.size());
        assertThrows(NoSuchElementException.class, list::removeFirst);
        assertThrows(NoSuchElementException.class, list::removeLast);
        assertThrows(NoSuchElementException.class, list::getFirst);
        assertThrows(NoSuchElementException.class, list::getLast);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
    }

    @Test
    void testAddFirst() {
        list.addFirst(100);
        assertEquals(1, list.size());
        assertEquals(100, list.getFirst());
        assertEquals(100, list.getLast());

        list.addFirst(50);
        assertEquals(2, list.size());
        assertEquals(50, list.getFirst());
        assertEquals(100, list.getLast());
    }

    @Test
    void testAddLast() {
        list.addLast(100);
        assertEquals(1, list.size());
        assertEquals(100, list.getFirst());
        assertEquals(100, list.getLast());

        list.addLast(50);
        assertEquals(2, list.size());
        assertEquals(100, list.getFirst());
        assertEquals(50, list.getLast());
    }

    @Test
    void testAddByIndex() {
        list.add(0, 100);
        assertEquals(100, list.get(0));

        list.add(1, 50);
        assertEquals(50, list.get(1));

        list.add(1, 75);
        assertEquals(75, list.get(1));
    }

    @Test
    void testRemoveFirst() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());

        list.addLast(100);
        list.addLast(50);

        assertEquals(100, list.removeFirst());
        assertEquals(1, list.size());
        assertEquals(50, list.getFirst());

        assertEquals(50, list.removeFirst());
        assertEquals(0, list.size());
    }

    @Test
    void testRemoveLast() {
        assertThrows(NoSuchElementException.class, () -> list.removeLast());

        list.addLast(100);
        list.addLast(50);

        assertEquals(50, list.removeLast());
        assertEquals(1, list.size());

        assertEquals(100, list.removeLast());
        assertEquals(0, list.size());
    }

    @Test
    void RemoveByIndex() {
        assertThrows(NoSuchElementException.class, () -> list.remove(0));

        list.addLast(100);
        list.addLast(75);
        list.addLast(50);

        assertEquals(75, list.remove(1));
        assertEquals(100, list.remove(0));
        assertEquals(50, list.remove(0));
    }

    @Test
    void testEdgeCases() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 100));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, 100));

        list.addFirst(100);
        assertEquals(100, list.removeFirst());
        assertEquals(0, list.size());

        list.addLast(100);
        assertEquals(100, list.removeLast());
        assertEquals(0, list.size());
    }

    @Test
    void testGeneral() {
        list.addFirst(100);
        list.addLast(200);
        list.addFirst(20);
        list.addLast(300);
        list.add(2, 75);

        assertEquals(5, list.size());
        assertEquals(20, list.get(0));
        assertEquals(100, list.get(1));
        assertEquals(200, list.get(3));
        assertEquals(300, list.get(4));
        assertEquals(75, list.get(2));

        list.removeFirst();
        assertEquals(100, list.getFirst());

        list.removeLast();
        assertEquals(200, list.getLast());

        list.remove(1);
        assertEquals(100, list.getFirst());
        assertEquals(200, list.getLast());
    }
}
