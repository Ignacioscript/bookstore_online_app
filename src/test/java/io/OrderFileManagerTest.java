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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderFileManagerTest {

    private OrderFileManager orderFileManager;

    private static final String ORDER_FILE = "src/test/resources/order_test_file.txt";
    private static final String CUSTOMER_FILE = "src/test/resources/customers_test_file.txt";

    @BeforeEach
    void setOrderFileManager() {
        CustomerFileManager customerFileManager = new CustomerFileManager(CUSTOMER_FILE);
        orderFileManager = new OrderFileManager(ORDER_FILE, CUSTOMER_FILE);

    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(CUSTOMER_FILE));
        Files.deleteIfExists(Path.of(ORDER_FILE));

    }

    @Test
    @DisplayName("testing Saving and Loading OrderFile")
    void testSaveAndLoadOrderFile() throws IOException {
        List<Customer> customers = Arrays.asList(
                new Customer("Alan", "Harper", "alan@mail.com", "555-111-22-33", "fake St 123", LocalDate.now(), 20),
                new Customer("Charlie", "Harper", "charlie@mail.com", "555-111-33-44", "fake St 321", LocalDate.now(), 10)
        );

        Customer customer1 = customers.get(0);
        Customer customer2 = customers.get(1);

        List<Order> orders = Arrays.asList(
                new Order(customer1, LocalDate.now(), 29.99, "Credit Card", Status.PENDING),
                new Order(customer2, LocalDate.now(), 43.79, "Credit Card", Status.SHIPPED)
        );


        //Act
        orderFileManager.save(orders);
        List<Order> loadedOrders = orderFileManager.load();

        assertEquals(orders.size(), loadedOrders.size(), "Should be same size");
    }




}