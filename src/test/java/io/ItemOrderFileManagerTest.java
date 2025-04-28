package io;

import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.io.CustomerFileManager;
import org.ignacioScript.co.io.ItemOrderFileManager;
import org.ignacioScript.co.io.OrderFileManager;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.model.ItemOrder;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemOrderFileManagerTest {

    private static final String BOOK_FILE = "src/test/resources/test_books.csv";
    private static final String CUSTOMER_FILE = "src/test/resources/test_customers.csv";
    private static final String ORDER_FILE = "src/test/resources/test_orders.csv";
    private static final String ITEMORDER_FILE = "src/test/resources/test_itemorders.csv";

    private BookFileManager bookFileManager;
    private CustomerFileManager customerFileManager;
    private OrderFileManager orderFileManager;
    private ItemOrderFileManager itemOrderFileManager;

    private Customer testCustomer;
    private Book testBook;
    private Order testOrder;
    private ItemOrder testItemOrder;

    @BeforeEach
    void setUp() throws IOException {
        // Create dummy files if they don't exist
        createFileIfNotExists(BOOK_FILE);
        createFileIfNotExists(CUSTOMER_FILE);
        createFileIfNotExists(ORDER_FILE);
        createFileIfNotExists(ITEMORDER_FILE);

        // Initialize managers
        bookFileManager = new BookFileManager(BOOK_FILE);
        customerFileManager = new CustomerFileManager(CUSTOMER_FILE);
        orderFileManager = new OrderFileManager(ORDER_FILE, CUSTOMER_FILE);
        itemOrderFileManager = new ItemOrderFileManager(ITEMORDER_FILE, BOOK_FILE, ORDER_FILE, CUSTOMER_FILE);

        // Clean previous test data
        clearFile(BOOK_FILE);
        clearFile(CUSTOMER_FILE);
        clearFile(ORDER_FILE);
        clearFile(ITEMORDER_FILE);

        // Setup test data
        setupTestData();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(BOOK_FILE));
        Files.deleteIfExists(Path.of(CUSTOMER_FILE));
        Files.deleteIfExists(Path.of(ORDER_FILE));
        Files.deleteIfExists(Path.of(ITEMORDER_FILE));
    }

    private void setupTestData() throws IOException {
        // Create test customer
        testCustomer = new Customer(
                "John", "Doe", "john.doe@example.com",
                "1234567890", "123 Street Name",
                LocalDate.now(), 50);
        testCustomer.setCustomerId(1);
        customerFileManager.save(List.of(testCustomer));

        // Create test book
        testBook = new Book(
                "123-456-789", "Test Book", "A book for testing",
                "Test Publisher", LocalDate.now(), 29.99, 10);
        testBook.setBookId(1);
        bookFileManager.save(List.of(testBook));

        // Create test order
        testOrder = new Order(testCustomer, LocalDate.now(), 59.98,
                "CREDIT_CARD", Status.PENDING);
        testOrder.setOrderId(1);
        orderFileManager.save(List.of(testOrder));

        // Create test item order
        testItemOrder = new ItemOrder(testBook, testOrder, 2, 29.99);
        testItemOrder.setItemOrderId(1);
    }

    @Test
    @DisplayName("Test saving and loading item orders")
    void testSaveAndLoadItemOrders() throws IOException {
        // Act
        itemOrderFileManager.save(List.of(testItemOrder));
        List<ItemOrder> loadedItemOrders = itemOrderFileManager.load();

        // Assert
        assertEquals(1, loadedItemOrders.size(), "Should load exactly one item order");
        ItemOrder loaded = loadedItemOrders.get(0);
        assertItemOrderEquals(testItemOrder, loaded);
    }

    @Test
    @DisplayName("Test handling empty item order list")
    void testSaveAndLoadEmptyItemOrders() throws IOException {
        // Act
        itemOrderFileManager.save(Collections.emptyList());
        List<ItemOrder> loadedItemOrders = itemOrderFileManager.load();

        // Assert
        assertTrue(loadedItemOrders.isEmpty(), "Loaded item orders should be empty");
    }

    @Test
    @DisplayName("Test deleting item order")
    void testDeleteItemOrder() throws IOException {
        // Arrange
        itemOrderFileManager.save(List.of(testItemOrder));

        // Act
        itemOrderFileManager.delete(testItemOrder.getItemOrderId());
        List<ItemOrder> remainingOrders = itemOrderFileManager.load();

        // Assert
        assertTrue(remainingOrders.isEmpty(), "No item orders should remain after deletion");
    }

    @Test
    @DisplayName("Test updating item order")
    void testUpdateItemOrder() throws IOException {
        // Arrange
        itemOrderFileManager.save(List.of(testItemOrder));
        ItemOrder updatedItemOrder = new ItemOrder(testBook, testOrder, 5, 39.99);
        updatedItemOrder.setItemOrderId(testItemOrder.getItemOrderId());

        // Act
        itemOrderFileManager.update(updatedItemOrder);
        List<ItemOrder> loadedItemOrders = itemOrderFileManager.load();

        // Assert
        assertEquals(1, loadedItemOrders.size(), "Should have one item order");
        ItemOrder loaded = loadedItemOrders.get(0);
        assertEquals(5, loaded.getQuantity(), "Quantity should be updated");
        assertEquals(39.99, loaded.getUnitPrice(), "Unit price should be updated");
    }

    @Test
    @DisplayName("Test getting item order by ID")
    void testGetItemOrderById() throws IOException {
        // Arrange
        itemOrderFileManager.save(List.of(testItemOrder));

        // Act
        ItemOrder retrieved = itemOrderFileManager.getById(testItemOrder.getItemOrderId());

        // Assert
        assertNotNull(retrieved, "Retrieved item order should not be null");
        assertItemOrderEquals(testItemOrder, retrieved);
    }

    @Test
    @DisplayName("Test getting non-existent item order")
    void testGetNonExistentItemOrder() {
        // Act
        ItemOrder retrieved = itemOrderFileManager.getById(-1);

        // Assert
        assertNull(retrieved, "Non-existent item order should return null");
    }

    @Test
    @DisplayName("Test multiple item orders")
    void testMultipleItemOrders() throws IOException {
        // Arrange
        ItemOrder secondItemOrder = new ItemOrder(testBook, testOrder, 3, 19.99);
        secondItemOrder.setItemOrderId(2);
        List<ItemOrder> multipleOrders = Arrays.asList(testItemOrder, secondItemOrder);

        // Act
        itemOrderFileManager.save(multipleOrders);
        List<ItemOrder> loadedOrders = itemOrderFileManager.load();

        // Assert
        assertEquals(2, loadedOrders.size(), "Should load two item orders");
        assertEquals(3, loadedOrders.get(1).getQuantity(), "First order quantity should match");
        assertEquals(3, loadedOrders.get(1).getQuantity(), "Second order quantity should match");
    }

    private void createFileIfNotExists(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private void clearFile(String filename) throws IOException {
        FileWriter fw = new FileWriter(filename, false);
        fw.write("");
        fw.close();
    }

    private void assertItemOrderEquals(ItemOrder expected, ItemOrder actual) {
        assertEquals(expected.getItemOrderId(), actual.getItemOrderId(), "ItemOrder ID should match");
        assertEquals(expected.getQuantity(), actual.getQuantity(), "Quantity should match");
        assertEquals(expected.getUnitPrice(), actual.getUnitPrice(), "Unit price should match");
        assertEquals(expected.getBook().getBookId(), actual.getBook().getBookId(), "Book ID should match");
        assertEquals(expected.getOrder().getOrderId(), actual.getOrder().getOrderId(), "Order ID should match");
        assertEquals(expected.getOrder().getCustomer().getFirstName(),
                actual.getOrder().getCustomer().getFirstName(), "Customer first name should match");
    }
}