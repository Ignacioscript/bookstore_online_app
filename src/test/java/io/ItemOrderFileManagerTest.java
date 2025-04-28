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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemOrderFileManagerTest {

    private static final String BOOK_FILE = "test_books.csv";
    private static final String CUSTOMER_FILE = "test_customers.csv";
    private static final String ORDER_FILE = "test_orders.csv";
    private static final String ITEMORDER_FILE = "test_itemorders.csv";

    private BookFileManager bookFileManager;
    private CustomerFileManager customerFileManager;
    private OrderFileManager orderFileManager;
    private ItemOrderFileManager itemOrderFileManager;

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
    }

    @Test
    void testSaveAndLoadItemOrder() throws IOException {
        // Prepare a customer
        Customer customer = new Customer(
                "John", "Doe", "john.doe@example.com", "1234567890",
                "123 Street Name", LocalDate.now(), 50
        );
        customer.setCustomerId(1);
        customerFileManager.save(List.of(customer));

        // Prepare a book
        Book book = new Book(
                "123-456-789", "Test Book", "A book for testing",
                "Test Publisher", LocalDate.now(), 29.99, 10
        );
        book.setBookId(1);
        bookFileManager.save(List.of(book));

        // Prepare an order
        Order order = new Order(customer, LocalDate.now(), 59.98, "CREDIT_CARD", Status.PENDING);
        order.setOrderId(1);
        orderFileManager.save(List.of(order));

        // Create an ItemOrder
        ItemOrder itemOrder = new ItemOrder(book, order, 2, 29.99);
        itemOrder.setItemOrderId(1);
        itemOrderFileManager.save(List.of(itemOrder));

        // Load ItemOrder
        List<ItemOrder> loadedItemOrders = itemOrderFileManager.load();
        assertEquals(1, loadedItemOrders.size(), "Should load exactly one item order");

        ItemOrder loaded = loadedItemOrders.get(0);

        // Validate loaded ItemOrder
        assertEquals(itemOrder.getItemOrderId(), loaded.getItemOrderId(), "ItemOrder ID should match");
        assertEquals(itemOrder.getQuantity(), loaded.getQuantity(), "Quantity should match");
        assertEquals(itemOrder.getUnitPrice(), loaded.getUnitPrice(), "Unit price should match");
        assertEquals(itemOrder.getBook().getBookId(), loaded.getBook().getBookId(), "Book ID should match");
        assertEquals(itemOrder.getOrder().getOrderId(), loaded.getOrder().getOrderId(), "Order ID should match");

        // Also check Customer details inside loaded Order
        assertEquals(customer.getFirstName(), loaded.getOrder().getCustomer().getFirstName(), "Customer first name should match");
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
}
