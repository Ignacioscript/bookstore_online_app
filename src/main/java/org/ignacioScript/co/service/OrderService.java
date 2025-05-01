package org.ignacioScript.co.service;

import org.ignacioScript.co.io.OrderFileManager;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.OrderValidator;

import java.io.IOException;
import java.util.List;

public class OrderService {

    private final OrderFileManager orderFileManager;
    private final String DEFAULT_FILE_PATH = "src/main/resources/orders.csv";

    public OrderService(OrderFileManager orderFileManager) {
        this.orderFileManager = orderFileManager;
    }

    public OrderService() {
        this.orderFileManager = new OrderFileManager(DEFAULT_FILE_PATH, "src/main/resources/customers.csv");
    }

    public void saveOrder(Order order) {
        validateOrder(order);

        orderFileManager.append(order);
        FileLogger.logInfo("OrderService - Order saved successfully");
    }

    public void updateOrder(Order order) {
        validateOrder(order);

        Order existingOrder = findOrderById(order.getOrderId());
        if (existingOrder == null) {
            throw new IllegalArgumentException("Order with ID " + order.getOrderId() + " does not exist");
        }

        orderFileManager.update(order);
        FileLogger.logInfo("OrderService - Order updated successfully: " + order);
    }

    public void deleteOrder(int id) {
        Order existingOrder = findOrderById(id);
        if (existingOrder == null) {
            throw new IllegalArgumentException("Order with ID " + id + " does not exist");
        }

        orderFileManager.delete(id);
        FileLogger.logInfo("OrderService - Order deleted successfully: " + existingOrder);
    }

    public Order findOrderById(int id) {
        OrderValidator.validateId(id);

        try {
            Order order = orderFileManager.getById(id);
            if (order == null) {
                throw new IllegalArgumentException("Order with ID: " + id + " does not exist");
            }

            FileLogger.logInfo("OrderService - Successfully found order with ID: " + id);
            return order;

        } catch (RuntimeException e) {
            FileLogger.logError("OrderService - Error finding order: " + e.getMessage());
            throw e;
        }
    }

    public List<Order> getAllOrders() {
        try {
            FileLogger.logInfo("OrderService - Fetching all orders from file");
            return orderFileManager.load();
        } catch (IOException e) {
            FileLogger.logError("OrderService - Error fetching orders: " + e.getMessage());
            throw new RuntimeException("Failed to load orders");
        }
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        OrderValidator.validateCustomer(order.getCustomer());
        OrderValidator.validateDate(order.getOrderDate());
        OrderValidator.validateTotal(order.getTotal());
        OrderValidator.validateProperNoun(order.getPaymentMethod());
        //TODO create validation for enum Status
    }
}