package model;

import org.ignacioScript.co.model.Customer;
import org.ignacioScript.co.model.Genre;
import org.ignacioScript.co.model.Order;
import org.ignacioScript.co.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DateTimeException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    Order orderTestObject;
    Customer customerTestObject;


    @BeforeEach
    public void setOrderTestObject() {
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
    }


    @Test
    public void shouldReturnAnOrderInstance() {
        assertNotNull(orderTestObject);
    }

    @Test
    @DisplayName("should Not return Exception for invalid or null Customer Object ")
    public void shouldNotReturnExceptionForCustomerObject() {
        assertDoesNotThrow( () -> orderTestObject.getCustomer());
    }

    // Test Date validations
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid date input or format ")
    @ValueSource(strings = {" ", "2024-02-30", "01-1985-23", "not-a-date", "2024/04/16"})
    public void shouldReturnExceptionForInvalidDate(String invalidDate) {
        assertThrows(DateTimeException.class, () -> {
            orderTestObject.setOrderDate(LocalDate.parse(invalidDate));
        });
    }
    @ParameterizedTest
    @DisplayName("Should throw Not Exception for valid date input or format ")
    @ValueSource(strings = {"2025-04-16"})
    public void shouldNotReturnExceptionForValidDate(String validDate) {

        assertDoesNotThrow( () -> {
            orderTestObject.setOrderDate(LocalDate.parse(validDate));
        });

    }


    // Test total inputs
    @ParameterizedTest
    @DisplayName("Should return Exception for invalid Total inputs")
    @ValueSource(doubles = { 0.0, -0, -25, -25.45, 56789087.567788})
    public void shouldReturnExceptionForInvalidTotal(double invalidAmount) {
        assertThrows(IllegalArgumentException.class, () -> orderTestObject.setTotal(invalidAmount));
    }


    // Test Status (Enum)
    @ParameterizedTest
    @ValueSource(strings = {"pending", "PAID", " ", "123"})
    public void shouldExceptionForInValidStatus(String invalidStatus) {
        assertThrows(IllegalArgumentException.class, () -> {Status.valueOf(invalidStatus);});
    }

    @ParameterizedTest
    @ValueSource(strings = {"PENDING", "PROCESSING", "SHIPPED", "DELIVERED"})
    public void shouldNotReturnExceptionForValidStatus(String validStatus) {
        assertDoesNotThrow(() -> Status.valueOf(validStatus));
    }







}

