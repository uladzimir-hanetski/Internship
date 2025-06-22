package org.example.util;

import org.example.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetricsTest {
    private final List<Order> orders = new ArrayList<>();
    Order order = new Order("8", LocalDateTime.now(), null, Collections.emptyList(), OrderStatus.NEW);
    Customer c1 = new Customer("1", "Man", "manemail@gmail.com", LocalDateTime.now(), 35, "Minsk");
    Customer c2 = new Customer("2", "Another man", "goodmail@gog.org", LocalDateTime.now(), 40, "Grodno");
    Customer c3 = new Customer("3", "Kan", "kilae@outlook.com", LocalDateTime.now(), 20, "Vitebsk");
    Customer c4 = new Customer("4", "Misha", "michael@site.net", LocalDateTime.now(), 25, "Vitebsk");

    @BeforeEach
    void initialize() {
        OrderItem book = new OrderItem("Brave new world", 3, 25.0, Category.BOOKS);
        OrderItem trousers = new OrderItem("Trousers", 20, 15.5, Category.CLOTHING);
        OrderItem tablet = new OrderItem("Applet", 5, 900.0, Category.ELECTRONICS);
        OrderItem toy = new OrderItem("Bear", 10, 35.0, Category.TOYS);
        OrderItem soap = new OrderItem("Soap", 15, 5.0, Category.HOME);
        OrderItem shampoo = new OrderItem("Shampoo", 2, 7.5, Category.BEAUTY);

        orders.add(new Order("1", LocalDateTime.now(), c1, new ArrayList<>(List.of(book, tablet)), OrderStatus.DELIVERED));
        orders.add(new Order("2", LocalDateTime.now(), c1, new ArrayList<>(List.of(soap)), OrderStatus.CANCELLED));
        orders.add(new Order("3", LocalDateTime.now(), c2, new ArrayList<>(List.of(trousers, toy, soap)), OrderStatus.DELIVERED));
        orders.add(new Order("4", LocalDateTime.now(), c2, new ArrayList<>(List.of(shampoo, tablet, toy)), OrderStatus.DELIVERED));
        orders.add(new Order("5", LocalDateTime.now(), c2, new ArrayList<>(List.of(shampoo, shampoo)), OrderStatus.DELIVERED));
        orders.add(new Order("6", LocalDateTime.now(), c3, new ArrayList<>(List.of(tablet, soap, book)), OrderStatus.DELIVERED));
        orders.add(new Order("7", LocalDateTime.now(), c4, new ArrayList<>(List.of(soap, toy, trousers)), OrderStatus.DELIVERED));
    }

    @Test
    void testOrdersCities() {
        List<String> cities = Metrics.ordersCities(orders);
        assertEquals(3, cities.size());
        assertTrue(cities.contains("Minsk"));
        assertTrue(cities.contains("Grodno"));
        assertTrue(cities.contains("Vitebsk"));
    }
/*
Book: 75
Trousers: 310
Tablet: 4500
Toy: 350
Soap: 75
Shampoo: 15
 */
/*
1: 4575
2: -
3: 735
4: 4865
5: 30
6: 4650
7: 735
*/
    @Test
    void testTotalIncome() {
        assertEquals( 15590, Metrics.totalIncome(orders));
        assertEquals(0, Metrics.totalIncome(Collections.emptyList()));
        assertEquals(0, Metrics.totalIncome(List.of(order)));
    }

    @Test
    void testMostPopularProduct() {
        assertEquals("Soap", Metrics.mostPopularProduct(orders));
        assertEquals("", Metrics.mostPopularProduct(Collections.emptyList()));
        assertEquals("", Metrics.mostPopularProduct(List.of(order)));
    }

    @Test
    void testAvgCheck() {
        assertEquals(2598.33, Metrics.avgCheck(orders), 0.01);
        assertEquals(0, Metrics.avgCheck(Collections.emptyList()));
        assertEquals(0, Metrics.avgCheck(List.of(order)));
    }

    @Test
    void testCustomersWithMoreThan5Orders() {
        assertTrue(Metrics.customersWithMoreThan5Orders(orders).isEmpty());

        orders.add(new Order("100", LocalDateTime.now(), c2, null, OrderStatus.DELIVERED));
        orders.add(new Order("101", LocalDateTime.now(), c2, null, OrderStatus.DELIVERED));
        orders.add(new Order("102", LocalDateTime.now(), c2, null, OrderStatus.DELIVERED));
        assertEquals("Another man", Metrics.customersWithMoreThan5Orders(orders).getFirst().getName());

        orders.add(new Order("15", LocalDateTime.now(), c1, null, OrderStatus.DELIVERED));
        orders.add(new Order("16", LocalDateTime.now(), c1, null, OrderStatus.SHIPPED));
        orders.add(new Order("17", LocalDateTime.now(), c1, null, OrderStatus.PROCESSING));
        orders.add(new Order("18", LocalDateTime.now(), c1, null, OrderStatus.CANCELLED));
        assertEquals(2, Metrics.customersWithMoreThan5Orders(orders).size());

        assertTrue(Metrics.customersWithMoreThan5Orders(Collections.emptyList()).isEmpty());
    }
}