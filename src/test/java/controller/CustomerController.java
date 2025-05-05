package controller;

import org.ignacioScript.co.controller.CustomerController;
import org.ignacioScript.co.io.CustomerFileManager;
import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CustomerControllerTest {

    private CustomerService customerService;
    private CustomerController customerController;
    private String tempFilePath = "src/test/resources/test_customers.csv";

    @BeforeEach
    void setUp() {
        // Set up a real CustomerService with a temporary file
        CustomerFileManager customerFileManager = new CustomerFileManager(tempFilePath);
        customerService = new CustomerService(customerFileManager);
        customerController = new CustomerController(new Scanner(System.in), customerService);

        // Seed the file with initial data
        List<Customer> initialCustomers = List.of(
                new Customer("John", "Doe", "john.doe@example.com", "1234567890", "123 Main St", LocalDate.now(), 50),
                new Customer("Jane", "Smith", "jane.smith@example.com", "9876543210", "456 Elm St", LocalDate.now(), 70)
        );
        customerFileManager.save(initialCustomers);
    }

    @Test
    @DisplayName("Test creating a new customer")
    void testCreateCustomer() {
        String input = "Alice\nBrown\nalice.brown@example.com\n5551234567\n789 Oak St\n30\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        customerController.createCustomer();

        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(3, customers.size(), "There should be 3 customers after adding a new one.");
        assertEquals("Alice", customers.get(2).getFirstName(), "The new customer's first name should be Alice.");
    }

    @Test
    @DisplayName("Test viewing all customers")
    void testViewCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size(), "There should be 2 customers initially.");
        assertEquals("John", customers.get(0).getFirstName(), "The first customer's name should be John.");
    }

    @Test
    @DisplayName("Test searching for a customer by email")
    void testSearchForCustomer() {
        String input = "john.doe@example.com\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        customerController.searchForCustomer();

        Customer customer = customerService.findCustomerByEmail("john.doe@example.com");
        assertNotNull(customer, "The customer with the given email should exist.");
        assertEquals("John", customer.getFirstName(), "The customer's first name should be John.");
    }

    @Test
    @DisplayName("Test deleting a customer")
    void testDeleteCustomer() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        customerController.deleteCustomer();

        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(1, customers.size(), "There should be 1 customer after deletion.");
        assertThrows(IllegalArgumentException.class, () -> customerService.findCustomerById(1), "The deleted customer should not exist.");
    }
}