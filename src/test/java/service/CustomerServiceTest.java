package service;

import org.ignacioScript.co.io.CustomerFileManager;
import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {

    private CustomerService customerServiceTest;
    private CustomerFileManager customerFileManagerTest;
    private String tempfile = "src/test/resources/test_customer.csv";
    private List<Customer> customerObjectList;

    @BeforeEach
    void setUp() {
        customerFileManagerTest = new CustomerFileManager(tempfile);
        customerServiceTest = new CustomerService(customerFileManagerTest);
        customerObjectList = Arrays.asList(

                new Customer("John", "Morrison", "john.morrison@email.com", "555-01-01-00", "123 fake street ", LocalDate.now(), 11),
                new Customer("Jane", "Smith", "jane.smith@email.com", "5550102", "345 fake street", LocalDate.now(), 12),
                new Customer("Bob", "Johnson", "bob.j@email.com", "55501013", "567 fake street", LocalDate.now(), 31),
                new Customer("Alice", "Brown", "alice.b@email.com", "5550104", "789 fake street", LocalDate.now(), 14),
                new Customer("Charlie", "Wilson", "charlie.w@email.com", "5550106", "012 fake street", LocalDate.now(), 15)

        );

        customerFileManagerTest.save(customerObjectList);

    }

    @Test
    void checkIfCustomerServiceItsNull() {
        assertNotNull(customerServiceTest);
    }

    @Test
    @DisplayName("Test appending a new Customer and retrieve it")
    void testSavingNewCustomer() throws IOException {
        Customer customerToAppend = new Customer("ignacio", "navarro", "nacho@mail.com", "3005551122", "cll 13-23 antioquia", LocalDate.now(), 55);

        customerServiceTest.saveCustomer(customerToAppend);

        List<Customer> loadedCustomer = customerFileManagerTest.load();
        assertEquals(6, loadedCustomer.size(), "The total id customers should now be 6");
        assertEquals("ignacio", loadedCustomer.get(5).getFirstName());
    }

    @Test
    @DisplayName("Test updating an existing customer")
    void testUpdateCustomer() throws IOException {

        // Arrange: Create an updated version of an existing customer
        Customer updatedCustomer = new Customer("John", "Doe", "john.doe@email.com", "5550101", "123 updated street", LocalDate.now(), 11);
        updatedCustomer.setCustomerId(1);
        // Act: Update the customer
        customerServiceTest.updateCustomer(updatedCustomer);

        // Assert: Verify the update is reflected in the file
        Customer loadedCustomer = customerServiceTest.findCustomerById(1);
        assertEquals("Doe", loadedCustomer.getLastName(), "The last name of the updated customer should be 'Doe'.");
        assertEquals("123 updated street", loadedCustomer.getAddress(), "The address of the updated customer should be '123 updated street'.");
    }


    @Test
    @DisplayName("Test retrieving all customers")
    void testGetAllCustomers() throws IOException {
        // Act: Retrieve all customers


       List<Customer> allCustomers = customerServiceTest.getAllCustomers();
        //List<Customer> allCustomers = customerFileManagerTest.load();

        // Assert: Verify all customers are loaded
        assertNotNull(allCustomers, "The list of customers should not be null.");
        assertEquals(5, allCustomers.size(), "The number of customers should be 5.");
    }

    @Test
    @DisplayName("Test deleting an existing customer")
    void testDeleteCustomer() throws IOException {
        // Act: Delete a customer by ID
        customerServiceTest.deleteCustomer(1);

        // Assert: Verify the customer was deleted
        List<Customer> loadedCustomers = customerFileManagerTest.load();
        assertEquals(4, loadedCustomers.size(), "The total number of customers should now be 4.");
        assertThrows(IllegalArgumentException.class, () -> customerServiceTest.findCustomerById(12), "Customer with ID 12 should no longer exist.");
    }

    @Test
    @DisplayName("Test finding a customer by ID")
    void testFindCustomerById() {
        // Act: Find a customer by a valid ID
        Customer foundCustomer = customerServiceTest.findCustomerById(1);

        // Assert: Verify the customer is returned correctly
        assertNotNull(foundCustomer, "The customer with ID 11 should be found.");
        assertEquals("John", foundCustomer.getFirstName(), "The first name of the customer should be 'John'.");
    }

    @Test
    @DisplayName("Test finding a customer by invalid ID")
    void testFindCustomerByInvalidId() {
        // Act & Assert: Attempt to find a customer with a non-existent ID
        assertThrows(IllegalArgumentException.class, () -> customerServiceTest.findCustomerById(999), "An exception should be thrown for non-existent customer ID.");
    }


    @Test
    @DisplayName("Test validation failure for null customer")
    void testSaveNullCustomer() {
        // Act & Assert: Attempt to save a null customer
        assertThrows(IllegalArgumentException.class, () -> customerServiceTest.saveCustomer(null), "Saving a null customer should throw an exception.");
    }

    @Test
    @DisplayName("Test updating a customer that does not exist")
    void testUpdateNonExistentCustomer() {
        // Arrange: Create a customer that doesn't exist in the file
        Customer nonExistentCustomer = new Customer("Non", "Existent", "nonexistent@email.com", "0000000000", "No address", LocalDate.now(), 99);

        // Act & Assert: Attempt to update the non-existent customer
        assertThrows(IllegalArgumentException.class, () -> customerServiceTest.updateCustomer(nonExistentCustomer), "Updating a non-existent customer should throw an exception.");
    }








}