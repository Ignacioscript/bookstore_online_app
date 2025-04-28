//package controller;
//
//import org.ignacioScript.co.controller.CustomerController;
//import org.ignacioScript.co.io.CustomerFileManager;
//import org.ignacioScript.co.model.Customer;
//import org.ignacioScript.co.service.CustomerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class CustomerControllerTest {
//
//    private CustomerController customerController;
//    private File tempFile;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        tempFile = File.createTempFile("customers", ".txt");
//        CustomerFileManager fileManager = new CustomerFileManager(tempFile.getAbsolutePath());
//        CustomerService customerService = new CustomerService(fileManager);
//        customerController = new CustomerController(customerService);
//    }
//
//    @Test
//    void testSaveAndLoadCustomers() {
//        // Arrange
//        List<Customer> customers = Arrays.asList(
//                new Customer("John", "Doe", "john@mail.com", "555-1234", "123 Street", null, 10)
//        );
//
//        // Act
//        customerController.saveCustomers(customers);
//        customerController.loadAndPrintCustomers();
//
//        // Assert
//        // No exceptions should be thrown, and the output should match the saved customers
//        assertDoesNotThrow(() -> customerController.loadAndPrintCustomers());
//    }
//}