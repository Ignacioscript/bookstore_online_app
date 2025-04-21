package model;

import org.ignacioScript.co.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;


public class BookTest {
    Book testBookObject;

    @BeforeEach
    public void setUp() {
        testBookObject = new Book(
                "9783161484100",
                "The Lost City",
                "Uncover ancient secrets in the Amazon",
                "HarperCollins Publishers",
                LocalDate.of(2021, 04, 20),
                12.99,
                20);
    }

    @Test
    @DisplayName("Check if object Book exist")
    public void shouldCreateBookInstance() {
        assertNotNull(testBookObject);

    }

    @Test
    @DisplayName("Return Correct Id")
    public void shouldReturnCorrectID(){
        assertEquals(1, testBookObject.getBookId());
    }

    @Test
    @DisplayName("Return correct ISBN")
    public void shouldReturnCorrectISBN() {
        assertEquals("9783161484100", testBookObject.getIsbn());
    }

    @Test
    @DisplayName("Return correct publisher")
    public void shouldReturnCorrectPublisher() {
        assertEquals("HarperCollins Publishers", testBookObject.getPublisher());
    }

    @Test
    @DisplayName("Return correct title")
    public void shouldReturnCorrectTitle() {
        assertEquals("The Lost City", testBookObject.getBookTitle());

    }

    @Test
    @DisplayName("Return correct price")
    public void shouldReturnCorrectPrice() {
        assertEquals(12.99 , testBookObject.getPrice());
    }

    @Test
    @DisplayName("Return correct description")
    public void shouldReturnCorrectDescription() {
        assertEquals("Uncover ancient secrets in the Amazon", testBookObject.getDescription());
    }

    @Test
    @DisplayName("Return correct publication date")
    public void shouldReturnCorrectDate() {
        assertEquals("2021-04-20", testBookObject.getPublicationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }
    @Test
    @DisplayName("Return correct stock")
    public void shouldReturnCorrectStock() {
        assertEquals(20, testBookObject.getStock());
    }


    // Validations



    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Invalid ISBN", "123456789", "978-3-16-148410-X"})
    @DisplayName("Should throw exception for invalid ISBN format")
    public void shouldThrowExceptionForInvalidISBNFormat(String invalidISBN) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setIsbn(invalidISBN));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Should throw exception for invalid title")
    public void shouldThrowExceptionForInvalidTitle(String invalidTitle) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setBookTitle(invalidTitle));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A very long title that exceeds the maximum allowed length of 100 characters. This title is invalid."})
    @DisplayName("Should throw exception for title exceeding max length")
    public void shouldThrowExceptionForTitleExceedingMaxLength(String longTitle) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setBookTitle(longTitle));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Should throw exception for invalid publisher")
    public void shouldThrowExceptionForInvalidPublisher(String invalidPublisher) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setPublisher(invalidPublisher));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A very long publisher name that exceeds the maximum allowed length of 100 characters. This publisher name is invalid."})
    @DisplayName("Should throw exception for publisher exceeding max length")
    public void shouldThrowExceptionForPublisherExceedingMaxLength(String longPublisher) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setPublisher(longPublisher));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -100.0, 0.5})
    @DisplayName("Should throw exception for invalid price")
    public void shouldThrowExceptionForInvalidPrice(double invalidPrice) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setPrice(invalidPrice));
    }

    @ParameterizedTest
    @ValueSource(doubles = {10_000_001.0, 20_000_000.0})
    @DisplayName("Should throw exception for price exceeding max limit")
    public void shouldThrowExceptionForPriceExceedingMaxLimit(double highPrice) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setPrice(highPrice));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    @DisplayName("Should throw exception for invalid stock")
    public void shouldThrowExceptionForInvalidStock(int invalidStock) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setStock(invalidStock));
    }

    @ParameterizedTest
    @ValueSource(ints = {1001, 2000})
    @DisplayName("Should throw exception for stock exceeding max limit")
    public void shouldThrowExceptionForStockExceedingMaxLimit(int highStock) {
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setStock(highStock));
    }

    @Test
    @DisplayName("Should throw exception for future publication date")
    public void shouldThrowExceptionForFuturePublicationDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> testBookObject.setPublicationDate(futureDate));
    }





}
