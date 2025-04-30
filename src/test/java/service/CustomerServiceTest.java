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
        customerServiceTest = new CustomerService();
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
        customerFileManagerTest.appendCustomer(customerToAppend);
        List<Customer> loadedCustomer = customerFileManagerTest.load();
        assertEquals("ignacio", loadedCustomer.get(5).getFirstName());
    }

    @Test
    @DisplayName("Test updating a Customer")
    void testUpdatingCustomerById() throws IOException {
        Customer customerToUpdate = new Customer("ignacio", "navarro", "nacho@mail.com", "3005551122", "cll 13-23 antioquia", LocalDate.now(), 55);


    }


}