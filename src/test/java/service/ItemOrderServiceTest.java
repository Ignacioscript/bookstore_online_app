package service;

import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.io.ItemOrderFileManager;
import org.ignacioScript.co.io.OrderFileManager;
import org.ignacioScript.co.model.*;
import org.ignacioScript.co.service.ItemOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemOrderServiceTest {

    private ItemOrderService itemOrderServiceTest;
    private ItemOrderFileManager itemOrderFileManagerTest;
    private String tempFile = "src/test/resources/test_item_orders.csv";
    private Customer customer;
    private Book book;
    private Order order;
    private List<ItemOrder> itemOrders;



    @BeforeEach
    void setUp() {
        // Initialize file managers
        BookFileManager bookFileManager = new BookFileManager("src/test/resources/test_books.csv");
        OrderFileManager orderFileManager = new OrderFileManager("src/test/resources/test_orders.csv", "src/test/resources/test_customer.csv");
        itemOrderFileManagerTest = new ItemOrderFileManager(tempFile, "src/test/resources/test_books.csv", "src/test/resources/test_orders.csv", "src/test/resources/test_customer.csv");
        itemOrderServiceTest = new ItemOrderService(itemOrderFileManagerTest);

        // Create and save test Book
        book = new Book("A1234567890123", "Book One", "Description One", "Publisher One", LocalDate.now(), 19.99, 10);
        book.setBookId(1);
        bookFileManager.save(List.of(book));

        // Create and save test Customer
        customer = new Customer("John", "Doe", "john.doe@example.com", "1234567890", "123 Main St", LocalDate.now(), 50);
        customer.setCustomerId(1);

        // Create and save test Order
        order = new Order(customer, LocalDate.now(), 100.0, "Credit Card", Status.PENDING);
        order.setOrderId(1);
        orderFileManager.save(List.of(order));

        // Verify that Book and Order are saved correctly
        assertNotNull(bookFileManager.getById(1), "Book with ID 1 should exist");
        assertNotNull(orderFileManager.getById(1), "Order with ID 1 should exist");

        // Create and save test ItemOrders
        itemOrders = Arrays.asList(
                new ItemOrder(book, order, 2, 19.99),
                new ItemOrder(book, order, 3, 29.99),
                new ItemOrder(book, order, 1, 39.99)
        );
        itemOrderFileManagerTest.save(itemOrders);
    }


    @Test
    void checkIfItemOrderServiceIsNotNull() {
        assertNotNull(itemOrderServiceTest);
    }

    @Test
    @DisplayName("Test saving a new ItemOrder")
    void testSavingNewItemOrder() throws IOException {
        // Create and save the required Book


       ItemOrder newItemOrder = new ItemOrder(book, order, 20, 59.99);
       newItemOrder.setItemOrderId(4);

       itemOrderServiceTest.saveItemOrder(newItemOrder);
       List<ItemOrder> loadedItemOrders = itemOrderFileManagerTest.load();
       assertEquals(4, loadedItemOrders.size());
    }

    @Test
    @DisplayName("Test updating an existing ItemOrder")
    void testUpdateItemOrder() throws IOException {
        ItemOrder updatedItemOrder = new ItemOrder(itemOrders.get(0).getBook(), itemOrders.get(0).getOrder(), 5, 19.99);
        updatedItemOrder.setItemOrderId(itemOrders.get(0).getItemOrderId());

        itemOrderServiceTest.updateItemOrder(updatedItemOrder);

        ItemOrder loadedItemOrder = itemOrderServiceTest.findItemOrderById(itemOrders.get(0).getItemOrderId());
        assertEquals(5, loadedItemOrder.getQuantity(), "The quantity should match");
    }

    @Test
    @DisplayName("Test finding an ItemOrder by ID")
    void testFindItemOrderById() {
        ItemOrder loadedItemOrder = itemOrderServiceTest.findItemOrderById(itemOrders.get(0).getItemOrderId());
        assertEquals(2, loadedItemOrder.getQuantity(), "The quantity should match");
    }

    @Test
    @DisplayName("Test retrieving all ItemOrders")
    void testRetrievingAllItemOrders() throws IOException {
        List<ItemOrder> allItemOrders = itemOrderServiceTest.getAllItemOrders();

        assertNotNull(allItemOrders, "The list of ItemOrders should not be null.");
        assertEquals(3, allItemOrders.size(), "The number of ItemOrders should be 3");
    }

    @Test
    @DisplayName("Test deleting an existing ItemOrder")
    void testDeleteItemOrder() throws IOException {
        itemOrderServiceTest.deleteItemOrder(itemOrders.get(0).getItemOrderId());

        List<ItemOrder> loadedItemOrders = itemOrderFileManagerTest.load();
        assertEquals(2, loadedItemOrders.size(), "The total number of ItemOrders should now be 2");
        assertThrows(IllegalArgumentException.class, () -> itemOrderServiceTest.findItemOrderById(itemOrders.get(0).getItemOrderId()), "ItemOrder with the ID no longer exists");
    }

    @ParameterizedTest
    @DisplayName("Test finding an ItemOrder with invalid ID")
    @ValueSource(ints = {999, 0, -1})
    void testFindItemOrderWithInvalidId(int id) {
        assertThrows(IllegalArgumentException.class, () -> itemOrderServiceTest.findItemOrderById(id), "Should throw an exception");
    }

    @Test
    @DisplayName("Test validation failure for null ItemOrder")
    void testSaveNullItemOrder() {
        assertThrows(IllegalArgumentException.class, () -> itemOrderServiceTest.saveItemOrder(null), "Saving a null ItemOrder should throw an exception");
    }

    @Test
    @DisplayName("Test updating an ItemOrder that does not exist")
    void testUpdateNonExistentItemOrder() {
        Book book = new Book("C1234567890123", "Book Three", "Description Three", "Publisher Three", LocalDate.now(), 39.99, 15);
        book.setBookId(3);

        Order order = new Order(null, LocalDate.now(), 300.0, "Cash", Status.SHIPPED);
        order.setOrderId(3);

        ItemOrder nonExistentItemOrder = new ItemOrder(book, order, 6, 39.99);
        nonExistentItemOrder.setItemOrderId(999);

        assertThrows(IllegalArgumentException.class, () -> itemOrderServiceTest.updateItemOrder(nonExistentItemOrder));
    }
}