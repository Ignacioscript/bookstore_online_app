package io;

import org.ignacioScript.co.io.CustomerFileManager;
import org.ignacioScript.co.io.OrderFileManager;
import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderFileManagerTest {

    private OrderFileManager orderFileManager;
    private static final String ORDER_FILE = "src/test/resources/order_test_file.txt";
    private static final String CUSTOMER_FILE = "src/test/resources/customers_test_file.txt";

    private Customer customer1;
    private Customer customer2;
    private List<Order> testOrders;

    @BeforeEach
    void setUp() {
        CustomerFileManager customerFileManager = new CustomerFileManager(CUSTOMER_FILE);
        orderFileManager = new OrderFileManager(ORDER_FILE, CUSTOMER_FILE);

        // Setup test customers
        customer1 = new Customer("Alan", "Harper", "alan@mail.com", "555-111-22-33",
                "fake St 123", LocalDate.now(), 20);
        customer2 = new Customer("Charlie", "Harper", "charlie@mail.com", "555-111-33-44",
                "fake St 321", LocalDate.now(), 10);

        // Setup test orders
        testOrders = Arrays.asList(
                new Order(customer1, LocalDate.now(), 29.99, "Credit Card", Status.PENDING),
                new Order(customer2, LocalDate.now(), 43.79, "Credit Card", Status.SHIPPED)
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(CUSTOMER_FILE));
        Files.deleteIfExists(Path.of(ORDER_FILE));
    }

    @Test
    @DisplayName("Test saving and loading orders")
    void testSaveAndLoadOrders() throws IOException {
        // Act
        orderFileManager.save(testOrders);
        List<Order> loadedOrders = orderFileManager.load();

        // Assert
        assertEquals(testOrders.size(), loadedOrders.size(), "Number of orders should match");
        for (int i = 0; i < testOrders.size(); i++) {
            Order original = testOrders.get(i);
            Order loaded = loadedOrders.get(i);

            assertEquals(original.getTotal(), loaded.getTotal(), "Order total should match");
            assertEquals(original.getPaymentMethod(), loaded.getPaymentMethod(), "Payment method should match");
            assertEquals(original.getStatus(), loaded.getStatus(), "Order status should match");
            assertEquals(original.getCustomer().getFirstName(), loaded.getCustomer().getFirstName(),
                    "Customer first name should match");
            assertEquals(original.getCustomer().getLastName(), loaded.getCustomer().getLastName(),
                    "Customer last name should match");
        }
    }

    @Test
    @DisplayName("Test handling empty order list")
    void testSaveAndLoadEmptyOrders() throws IOException {
        // Arrange
        List<Order> emptyOrders = Collections.emptyList();

        // Act
        orderFileManager.save(emptyOrders);
        List<Order> loadedOrders = orderFileManager.load();

        // Assert
        assertTrue(loadedOrders.isEmpty(), "Loaded orders should be empty");
    }

    @Test
    @DisplayName("Test deleting order")
    void testDeleteOrder() throws IOException {
        // Arrange
        orderFileManager.save(testOrders);
        Order orderToDelete = testOrders.get(0);

        // Act
        orderFileManager.delete(orderToDelete.getOrderId());
        List<Order> remainingOrders = orderFileManager.load();

        // Assert
        assertEquals(1, remainingOrders.size(), "Should have one order remaining");
        assertNotEquals(orderToDelete.getOrderId(), remainingOrders.get(0).getOrderId(),
                "Deleted order should not exist");
    }

    @Test
    @DisplayName("Test updating order")
    void testUpdateOrder() throws IOException {
        // Arrange
        orderFileManager.save(testOrders);
        Order orderToUpdate = testOrders.get(0);

        // Create updated order with same ID but different details
        Order updatedOrder = new Order(
                customer1,
                LocalDate.now(),
                99.99,  // new total
                "PayPal", // new payment method
                Status.DELIVERED // new status
        );
        updatedOrder.setOrderId(orderToUpdate.getOrderId());

        // Act
        orderFileManager.update(updatedOrder);
        List<Order> loadedOrders = orderFileManager.load();
        Order retrievedOrder = loadedOrders.stream()
                .filter(o -> o.getOrderId() == updatedOrder.getOrderId())
                .findFirst()
                .orElse(null);

        // Assert
        assertNotNull(retrievedOrder, "Updated order should exist");
        assertEquals(99.99, retrievedOrder.getTotal(), "Total should be updated");
        assertEquals("PayPal", retrievedOrder.getPaymentMethod(), "Payment method should be updated");
        assertEquals(Status.DELIVERED, retrievedOrder.getStatus(), "Status should be updated");
    }

    @Test
    @DisplayName("Test getting order by ID")
    void testGetOrderById() throws IOException {
        // Arrange
        orderFileManager.save(testOrders);
        int orderId = testOrders.get(0).getOrderId();

        // Act
        Order retrievedOrder = orderFileManager.getById(orderId);

        // Assert
        assertNotNull(retrievedOrder, "Retrieved order should not be null");
        assertEquals(orderId, retrievedOrder.getOrderId(), "Order ID should match");
        assertEquals(testOrders.get(0).getTotal(), retrievedOrder.getTotal(), "Order total should match");
        assertEquals(testOrders.get(0).getStatus(), retrievedOrder.getStatus(), "Order status should match");
    }

    @Test
    @DisplayName("Test order with invalid ID returns null")
    void testGetNonExistentOrderById() throws IOException {
        // Arrange
        orderFileManager.save(testOrders);

        // Act
        Order retrievedOrder = orderFileManager.getById(-1);

        // Assert
        assertNull(retrievedOrder, "Non-existent order should return null");
    }
}