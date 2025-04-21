package model;

import org.ignacioScript.co.model.*;
import org.ignacioScript.co.validation.AuthorBookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ItemOrderTest {

    ItemOrder itemOrderTestObject;
    Book bookTestObject;
    Customer customerTestObject;
    Order orderTestObject;

    @BeforeEach
    public void setItemOrderTestObject() {
        bookTestObject =  new Book(
                "9783161484100",
                "The Lost City",
                "Uncover ancient secrets in the Amazon",
                "HarperCollins Publishers",
                LocalDate.of(2021, 04, 20),
                12.99,
                20);

        customerTestObject = new Customer(
                "Alan",
                "Harper",
                "alan@mail.com",
                "555-111-22-33",
                "fake st 123",
                LocalDate.now(),
                10
        );

        orderTestObject = new Order(
                customerTestObject,
                LocalDate.now(),
                25.50,
                "credit card",
                Status.SHIPPED
        );




        itemOrderTestObject = new ItemOrder(
                bookTestObject,
                orderTestObject,
                20,
                12.99
                );
    }

    @Test
    public void shouldReturnAnInstance() {
        assertNotNull(itemOrderTestObject);
    }

    //TODO finish testing nulls objects
    @Test
    public void shouldReturnExceptionForNullParameters() {
       assertThrows(NullPointerException.class, () -> {
           itemOrderTestObject = new ItemOrder(
               null,
               orderTestObject,
               20,
               12.99
            );
       });
    }

    @Test
    @DisplayName("Should throw NullPointerException when Book is null")
    public void shouldThrowExceptionForNullBook() {
        assertThrows(NullPointerException.class, () -> itemOrderTestObject.setBook(null));
    }

    @Test
    @DisplayName("Should throw NullPointerException when Order is null")
    public void shouldThrowExceptionForNullOrder() {
        assertThrows(NullPointerException.class, () -> itemOrderTestObject.setOrder(null));
    }

    @Test
    @DisplayName("Should not throw exception for valid Book and Order")
    public void shouldNotThrowExceptionForValidBookAndOrder() {
        assertDoesNotThrow(() -> {
            itemOrderTestObject.setBook(bookTestObject);
            itemOrderTestObject.setOrder(orderTestObject);
        });
    }

    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid itemOrderId")
    @ValueSource(ints = {-1, 0, 1000000})
    public void shouldThrowExceptionForInvalidItemOrderId(int invalidId) {
        assertThrows(IllegalArgumentException.class, () -> itemOrderTestObject.getItemOrderId());

    }

    @ParameterizedTest
    @DisplayName("Should not throw exception for valid itemOrderId")
    @ValueSource(ints = {1, 10, 125})
    public void shouldNotThrowExceptionForValidItemOrderId(int validId) {
        assertDoesNotThrow(() -> itemOrderTestObject.getItemOrderId());
    }

    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid quantity")
    @ValueSource(ints = {-1, 0, 1000000})
    public void shouldThrowExceptionForInvalidQuantity(int invalidQuantity) {
        assertThrows(IllegalArgumentException.class, () -> itemOrderTestObject.setQuantity(invalidQuantity));

    }

    @ParameterizedTest
    @DisplayName("Should not throw exception for valid quantity")
    @ValueSource(ints = {1, 5, 20, 100})
    public void shouldNotThrowExceptionForValidQuantity(int validQuantity) {
        assertDoesNotThrow(() -> itemOrderTestObject.setQuantity(validQuantity));
    }

    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid unitPrice")
    @ValueSource(doubles = {0.0, -1.0, 0.99, 124.99, 124.999939393984748587584})
    public void shouldThrowExceptionForInvalidUnitPrice(double invalidUnitPrice) {
        assertThrows(IllegalArgumentException.class, () -> itemOrderTestObject.setUnitPrice(invalidUnitPrice));

    }

    @ParameterizedTest
    @DisplayName("Should not throw exception for valid unitPrice")
    @ValueSource(doubles = {5.5, 10, 9.99, 67.34})
    public void shouldNotThrowExceptionForValidUnitPrice(double validUnitPrice) {
        assertDoesNotThrow(() -> itemOrderTestObject.setUnitPrice(validUnitPrice));
    }

}
