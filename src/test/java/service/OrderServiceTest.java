package service;

import org.ignacioScript.co.io.OrderFileManager;
import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.model.Status;
import org.ignacioScript.co.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private OrderService orderServiceTest;
    private OrderFileManager orderFileManagerTest;
    private String tempFile = "src/test/resources/test_orders.csv";
    private String customerFile = "src/test/resources/test_customers.csv";
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orderFileManagerTest = new OrderFileManager(tempFile, customerFile);
        orderServiceTest = new OrderService(orderFileManagerTest);

        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "1234567890", "123 Main St", LocalDate.now(), 50);
        customer.setCustomerId(1);

        orders = Arrays.asList(
                new Order(customer, LocalDate.now(), 100.0, "Credit Card", Status.PENDING),
                new Order(customer, LocalDate.now(), 200.0, "PayPal", Status.SHIPPED),
                new Order(customer, LocalDate.now(), 300.0, "Cash", Status.PROCESSING)
        );

        orderFileManagerTest.save(orders);
    }

    @Test
    void checkIfOrderServiceIsNotNull() {
        assertNotNull(orderServiceTest);
    }

    @Test
    @DisplayName("Test saving a new order")
    void testSavingNewOrder() throws IOException {
        Customer customer = new Customer("Jane", "Smith", "jane.smith@example.com", "0987654321", "456 Elm St", LocalDate.now(), 30);
        customer.setCustomerId(2);

        Order newOrder = new Order(customer, LocalDate.now(), 400.0, "Debit Card", Status.PENDING);
        orderServiceTest.saveOrder(newOrder);

        List<Order> loadedOrders = orderFileManagerTest.load();
        assertEquals(4, loadedOrders.size(), "The total number of orders should now be 4");
        assertEquals(400.0, loadedOrders.get(3).getTotal());
    }

    @Test
    @DisplayName("Test updating an existing order")
    void testUpdateOrder() {
        Order updatedOrder = new Order(orders.get(0).getCustomer(), LocalDate.now(), 150.0, "Credit Card", Status.SHIPPED);
        updatedOrder.setOrderId(orders.get(0).getOrderId());

        orderServiceTest.updateOrder(updatedOrder);

        Order loadedOrder = orderServiceTest.findOrderById(orders.get(0).getOrderId());
        assertEquals(150.0, loadedOrder.getTotal(), "The order total should match");
    }

    @Test
    @DisplayName("Test finding an order by ID")
    void testFindOrderById() {
        Order loadedOrder = orderServiceTest.findOrderById(orders.get(0).getOrderId());
        assertEquals(100.0, loadedOrder.getTotal(), "The order total should match");
    }

    @Test
    @DisplayName("Test retrieving all orders")
    void testRetrievingAllOrders() {
        List<Order> allOrders = orderServiceTest.getAllOrders();

        assertNotNull(allOrders, "The list of orders should not be null.");
        assertEquals(3, allOrders.size(), "The number of orders should be 3");
    }

    @Test
    @DisplayName("Test deleting an existing order")
    void testDeleteOrder() throws IOException {
        orderServiceTest.deleteOrder(orders.get(0).getOrderId());

        List<Order> loadedOrders = orderFileManagerTest.load();
        assertEquals(2, loadedOrders.size(), "The total number of orders should now be 2");
        assertThrows(IllegalArgumentException.class, () -> orderServiceTest.findOrderById(orders.get(0).getOrderId()), "Order with the ID no longer exists");
    }

    @ParameterizedTest
    @DisplayName("Test finding an order with invalid ID")
    @ValueSource(ints = {999, 0, -1})
    void testFindOrderWithInvalidId(int id) {
        assertThrows(IllegalArgumentException.class, () -> orderServiceTest.findOrderById(id), "Should throw an exception");
    }

    @Test
    @DisplayName("Test validation failure for null order")
    void testSaveNullOrder() {
        assertThrows(IllegalArgumentException.class, () -> orderServiceTest.saveOrder(null), "Saving a null order should throw an exception");
    }

    @Test
    @DisplayName("Test updating an order that does not exist")
    void testUpdateNonExistentOrder() {
        Customer customer = new Customer("Jane", "Smith", "jane.smith@example.com", "0987654321", "456 Elm St", LocalDate.now(), 30);
        customer.setCustomerId(2);

        Order nonExistentOrder = new Order(customer, LocalDate.now(), 400.0, "Debit Card", Status.PENDING);
        nonExistentOrder.setOrderId(999);

        assertThrows(IllegalArgumentException.class, () -> orderServiceTest.updateOrder(nonExistentOrder));
    }
}