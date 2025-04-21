package model;

import org.ignacioScript.co.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;


import java.time.DateTimeException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    Customer customerTestObject;

    @BeforeEach
    public void setUpCustomer() {
        customerTestObject = new Customer(
                "Alan",
                "Harper",
                "alan@mail.com",
                "555-111-22-33",
                "fake st 123",
                LocalDate.now(),
                10);
    }

    @Test
    public void shouldReturnAnInstanceOfCustomer() {
        assertNotNull(customerTestObject);
    }


    @Test
    public void shouldReturnCustomer() {
        assertEquals(1, customerTestObject.getCustomerId());
        assertEquals("Alan", customerTestObject.getFirstName());
        assertEquals("Harper 1 ", customerTestObject.getLastName());

    }

    //Validations
    // TEST ID
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid ID inputs")
    @ValueSource(ints = {0, 10000000, -1})
    public void shouldReturnExceptionForInvalidId(int invalidId) {
        assertThrows(IllegalArgumentException.class, () -> customerTestObject.getCustomerId());
    }
    @ParameterizedTest
    @DisplayName("Should not throw exception for valid ID input")
    @ValueSource(ints = {1, 35, 100})
    public void shouldNotReturnExceptionForvalidId(int validId) {
        assertDoesNotThrow( () -> customerTestObject.getCustomerId());
    }



    // TEST Firs Name
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid first name inputs")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "1234", "@ignacio", "a"})
    public void shouldReturnExceptionForInvalidFirstName(String invalidName) {
        assertThrows(IllegalArgumentException.class, () -> customerTestObject.setFirstName(invalidName));
    }
    @ParameterizedTest
    @DisplayName("Should Not throw Exception for valid first name inputs")
    @ValueSource(strings = {"Rory O'Brian", "Bob Clark", "ignacio", "Marcela castro", "alan harper"})
    public void shouldNotReturnExceptionForValidFirstName(String validName) {
        assertDoesNotThrow(() -> customerTestObject.setFirstName(validName));
    }




    // Test for invalid last name
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid last name inputs")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "1234"})
    public void shouldThrowExceptionForInvalidLastName(String invalidName) {
    }
    @ParameterizedTest
    @DisplayName("Should Not throw Exception for valid last name inputs")
    @ValueSource(strings = {"Rory O'Brian", "Bob Clark", "ignacio", "Marcela castro", "alan harper"})
    public void shouldNotReturnExceptionForValidLastName(String validLastName) {
        assertDoesNotThrow(() -> customerTestObject.setLastName(validLastName));
    }



    // Test Mail validations
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid mail inputs")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "1234", "w", "@ignacio.com", "ignacio.mail.com"})
    public void shouldReturnExceptionForInvalidMail(String invalidMail) {
        assertThrows(IllegalArgumentException.class, () -> customerTestObject.setMail(invalidMail));
    }
    @ParameterizedTest
    @DisplayName("Should Not throw Exception for valid mail inputs")
    @ValueSource(strings = {"alan@mail.com", "alan123@mail.com", "alan-123@mail123.com"})
    public void shouldNotReturnExceptionForValidMail(String validMail) {
        assertDoesNotThrow(() -> customerTestObject.setMail(validMail));
    }




    // Test Phone Number Validations
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentExcepton for invalid phone number inputs")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "abcdf", "ABCD", "%$#*-", "12345678909876543123567111", "300.679.99.29"})
    public void shouldThrowExceptionForInvalidPhoneNumber(String invalidPhoneNumber) {
        assertThrows(IllegalArgumentException.class, () -> customerTestObject.setPhoneNumber(invalidPhoneNumber));
    }
    @ParameterizedTest
    @DisplayName("Should not throw exception for valid phone number inputs")
    @ValueSource(strings = {"3006769929", "300-679-99-29", "300 679 99 29"})
    public void shouldNotThrowExceptionForValidPhoneNumber(String validPhoneNumber) {
        assertDoesNotThrow(() -> customerTestObject.setPhoneNumber(validPhoneNumber));
    }



// TEST address validations
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid address inputs")
    @ValueSource(strings = {"", " ", "1234", "@invalidAddress", "Address!@#", "This address is way too long and exceeds the maximum allowed length of 100 characters which should not be valid"})
    public void shouldThrowExceptionForInvalidAddress(String invalidAddress) {
        assertThrows(IllegalArgumentException.class, () -> customerTestObject.setAddress(invalidAddress));
    }
    @ParameterizedTest
    @DisplayName("Should not throw exception for valid address inputs")
    @ValueSource(strings = {"123 Main St.", "Apt. 4B", "456 Elm Street, Springfield", "St. John's", "Los Angeles"})
    public void shouldNotThrowExceptionForValidAddress(String validAddress) {
        assertDoesNotThrow(() -> customerTestObject.setAddress(validAddress));
    }




    // Test Date validations
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid date input or format ")
    @ValueSource(strings = {" ", "2024-02-30", "01-1985-23", "not-a-date", "2024/04/16", "2025-06-01"})
    public void shouldReturnExceptionForInvalidDate(String invalidDate) {
        assertThrows(DateTimeException.class, () -> {
            customerTestObject.setRegistrationDate(LocalDate.parse(invalidDate));
        });
    }
    @ParameterizedTest
    @DisplayName("Should throw Not Exception for valid date input or format ")
    @ValueSource(strings = {"2025-04-16"})
    public void shouldNotReturnExceptionForValidDate(String validDate) {

        assertDoesNotThrow( () -> {
            customerTestObject.setRegistrationDate(LocalDate.parse(validDate));
        });

    }


    // TEST loyalty points validations
    @ParameterizedTest
    @DisplayName("Should throw IllegalArgumentException for invalid loyalty points inputs")
    @ValueSource(ints = {0, 10000000, -1})
    public void shouldReturnExceptionForInvalidLoyaltyPoints(int invalidPoints) {
        assertThrows(IllegalArgumentException.class, () -> customerTestObject.setLoyaltyPoints(invalidPoints));
    }
    @ParameterizedTest
    @DisplayName("Should Not throw Exception for valid loyalty points inputs")
    @ValueSource(ints = {10, 100, 55, 15})
    public void shouldNotReturnExceptionForValidLoyaltyPoints(int validPoints) {
        assertDoesNotThrow( () -> customerTestObject.setLoyaltyPoints(validPoints));
    }








}
