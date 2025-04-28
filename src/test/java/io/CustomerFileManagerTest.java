package io;

import org.ignacioScript.co.io.CustomerFileManager;
import org.ignacioScript.co.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static  org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CustomerFileManagerTest {

    private static final String tempFile = "src/test/resources/customers_test_file.txt";
    private static CustomerFileManager customerFileManagerTest;

    @BeforeEach
    void setUp() {
        customerFileManagerTest = new CustomerFileManager(tempFile);
    }

//    @AfterEach
//    void tearDown() throws IOException {
//        Files.deleteIfExists(Path.of(tempFile));
//    }

    @Test
    @DisplayName("Test Saving and loading customers")
    void testSaveAndLoadCustomers() throws IOException{
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

        for (int i = 0; i < customersToSave.size(); i ++) {
            assertEquals(customersToSave.get(i).getFirstName(), loadedCustomers.get(i).getFirstName());
            assertEquals(customersToSave.get(i).getLastName(), loadedCustomers.get(i).getLastName());
            assertEquals(customersToSave.get(i).getMail(), loadedCustomers.get(i).getMail());
            assertEquals(customersToSave.get(i).getAddress(), loadedCustomers.get(i).getAddress());
            assertEquals(customersToSave.get(i).getLoyaltyPoints(), loadedCustomers.get(i).getLoyaltyPoints());
        }

    }


}
