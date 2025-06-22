package org.example.util;

import org.example.entity.Customer;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.OrderStatus;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Metrics {

    public static List<String> ordersCities(List<Order> orders) {
        return orders.stream().map(x -> x.getCustomer().getCity())
                .distinct().toList();
    }

    public static double totalIncome(List<Order> orders) {
        return orders.stream().filter(x -> x.getOrderStatus() == OrderStatus.DELIVERED)
                .flatMap(x -> x.getItems().stream())
                .map(x -> x.getQuantity() * x.getPrice())
                .reduce(0.0, Double::sum);
    }

    public static String mostPopularProduct(List<Order> orders) {
        return orders.stream().flatMap(x -> x.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProductName, Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("");
    }

    public static double avgCheck(List<Order> orders) {
        return orders.stream().filter(x -> x.getOrderStatus() == OrderStatus.DELIVERED)
                .mapToDouble(x -> x.getItems().stream().mapToDouble(y -> y.getPrice() * y.getQuantity())
                .sum()).average().orElse(0.0);
    }

    public static List<Customer> customersWithMoreThan5Orders(List<Order> orders) {
        return orders.stream().collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
                .entrySet().stream().filter(x -> x.getValue() > 5)
                .map(Map.Entry::getKey).toList();
    }
}
