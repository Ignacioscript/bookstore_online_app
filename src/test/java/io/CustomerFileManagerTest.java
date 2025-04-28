package io;

import org.ignacioScript.co.io.CustomerFileManager;
import org.ignacioScript.co.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomerFileManagerTest {

    private static final String tempFile = "src/test/resources/customers_test_file.txt";
    private static CustomerFileManager customerFileManagerTest;

    @BeforeEach
    void setUp() {
        customerFileManagerTest = new CustomerFileManager(tempFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(tempFile));
    }

    @Test
    @DisplayName("Test Saving and loading customers")
    void testSaveAndLoadCustomers() throws IOException {
        //Arrange
        List<Customer> customersToSave = Arrays.asList(
                new Customer("Joe", "Doe", "joe@mail.com", "555-111-22-33", "fake st 123", LocalDate.now(), 10),
                new Customer("Jane", "Dolly", "jane@mail.com", "555-222-22-33", "fake st 345", LocalDate.now(), 20)
        );

        //Act
        customerFileManagerTest.save(customersToSave);
        List<Customer> loadedCustomers = customerFileManagerTest.load();

        //Assert
        assertEquals(customersToSave.size(), loadedCustomers.size(), "The number of customers should match");

        for (int i = 0; i < customersToSave.size(); i++) {
            assertEquals(customersToSave.get(i).getFirstName(), loadedCustomers.get(i).getFirstName());
            assertEquals(customersToSave.get(i).getLastName(), loadedCustomers.get(i).getLastName());
            assertEquals(customersToSave.get(i).getMail(), loadedCustomers.get(i).getMail());
            assertEquals(customersToSave.get(i).getAddress(), loadedCustomers.get(i).getAddress());
            assertEquals(customersToSave.get(i).getLoyaltyPoints(), loadedCustomers.get(i).getLoyaltyPoints());
        }
    }

    @Test
    @DisplayName("Test handling of empty customer list")
    void testSaveAndLoadEmptyCustomers() throws IOException {
        // Arrange
        List<Customer> emptyCustomers = Collections.emptyList();

        // Act
        customerFileManagerTest.save(emptyCustomers);
        List<Customer> loadedCustomers = customerFileManagerTest.load();

        // Assert
        assertTrue(loadedCustomers.isEmpty(), "Loaded customers should be empty");
    }

    @Test
    @DisplayName("Test deleting customer")
    void testDeleteCustomer() throws IOException {
        // Arrange
        List<Customer> customersToSave = Arrays.asList(
                new Customer("Joe", "Doe", "joe@mail.com", "555-111-22-33", "fake st 123", LocalDate.now(), 10),
                new Customer("Jane", "Dolly", "jane@mail.com", "555-222-22-33", "fake st 345", LocalDate.now(), 20)
        );

        // Act
        customerFileManagerTest.save(customersToSave);
        Customer customerToRemove = customersToSave.get(0);

        customerFileManagerTest.delete(customerToRemove.getCustomerId());
        List<Customer> loadedCustomers = customerFileManagerTest.load();

        // Assert
        assertEquals(1, loadedCustomers.size(), "One customer must remain after removing");
        assertNotEquals(customerToRemove.getFirstName(), loadedCustomers.get(0).getFirstName(), "Removed customer should not exist");
    }

    @Test
    @DisplayName("Test updating a customer")
    void testUpdateCustomer() throws IOException {
        // Arrange
        List<Customer> customersToSave = Arrays.asList(
                new Customer("Joe", "Doe", "joe@mail.com", "555-111-22-33", "fake st 123", LocalDate.now(), 10),
                new Customer("Jane", "Dolly", "jane@mail.com", "555-222-22-33", "fake st 345", LocalDate.now(), 20)
        );

        customerFileManagerTest.save(customersToSave);

        // Create updated customer with same ID but different details
        Customer customerToUpdate = new Customer(
                "John", "Smith", "john@mail.com", "555-333-44-55",
                "new address 789", LocalDate.now(), 30
        );
        customerToUpdate.setCustomerId(customersToSave.get(0).getCustomerId());

        // Act
        customerFileManagerTest.update(customerToUpdate);
        List<Customer> loadedCustomers = customerFileManagerTest.load();

        // Assert
        assertEquals(customersToSave.size(), loadedCustomers.size(), "The number of customers should remain the same");
        assertEquals("John", loadedCustomers.get(0).getFirstName());
        assertEquals("Smith", loadedCustomers.get(0).getLastName());
        assertEquals("john@mail.com", loadedCustomers.get(0).getMail());
        assertEquals("555-333-44-55", loadedCustomers.get(0).getPhoneNumber() );
        assertEquals("new address 789", loadedCustomers.get(0).getAddress());
        assertEquals(30, loadedCustomers.get(0).getLoyaltyPoints());
    }

    @Test
    @DisplayName("Test getting customer by ID")
    void testGetCustomerById() throws IOException {
        // Arrange
        List<Customer> customersToSave = Arrays.asList(
                new Customer("Joe", "Doe", "joe@mail.com", "555-111-22-33", "fake st 123", LocalDate.now(), 10),
                new Customer("Jane", "Dolly", "jane@mail.com", "555-222-22-33", "fake st 345", LocalDate.now(), 20)
        );

        customerFileManagerTest.save(customersToSave);

        // Act
        Customer retrievedCustomer = customerFileManagerTest.getById(0); // Get first customer

        // Assert
        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");
        assertEquals("Joe", retrievedCustomer.getFirstName());
        assertEquals("Doe", retrievedCustomer.getLastName());
        assertEquals("joe@mail.com", retrievedCustomer.getMail());
    }
}