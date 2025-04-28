//package service;
//
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
//public class CustomerServiceTest {
//
//    private CustomerService customerService;
//    private File tempFile;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        tempFile = File.createTempFile("customers", ".txt");
//        CustomerFileManager fileManager = new CustomerFileManager(tempFile.getAbsolutePath());
//        customerService = new CustomerService(fileManager);
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
//        customerService.saveCustomers(customers);
//        List<Customer> loadedCustomers = customerService.loadCustomers();
//
//        // Assert
//        assertEquals(customers, loadedCustomers);
//    }
//
//    @Test
//    void testSaveCustomersThrowsException() {
//        // Arrange
//        tempFile.setReadOnly(); // Make the file read-only to simulate an error
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> customerService.saveCustomers(List.of()));
//    }
//}