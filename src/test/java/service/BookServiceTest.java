package service;

import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BookServiceTest {

    private BookService bookServiceTest;
    private BookFileManager bookFileManagerTest;
    private String tempFile = "src/test/resources/test_books.csv";
    private List<Book> books;

    @BeforeEach
    void setUp() {
        bookFileManagerTest = new BookFileManager(tempFile);
        bookServiceTest = new BookService(bookFileManagerTest);
        books = Arrays.asList(
                new Book("A1234567890123", "Book One", "Description One", "Publisher One", LocalDate.of(2023, 1, 1), 19.99, 10),
                new Book("A9876543210987", "Book Two", "Description Two", "Publisher Two", LocalDate.of(2022, 5, 15), 29.99, 5),
                new Book("B1234567890123", "Book Three", "Description Three", "Publisher Three", LocalDate.of(2021, 3, 10), 39.99, 15)
        );

        bookFileManagerTest.save(books);
    }

    @Test
    void checkIfBookServiceIsNotNull() {
        assertNotNull(bookServiceTest);
    }

    @Test
    @DisplayName("Test appending a new book and retrieving it")
    void testSavingNewBook() throws IOException {
        Book bookToAppend = new Book("C1234567890123", "Book Four", "Description for this book and must have more than 20 characters  ", "Publisher Four", LocalDate.of(2020, 7, 20), 49.99, 20);
        bookServiceTest.saveBook(bookToAppend);

        List<Book> loadedBooks = bookFileManagerTest.load();
        assertEquals(4, loadedBooks.size(), "The total number of books should now be 4");
        assertEquals("Book Four", loadedBooks.get(3).getBookTitle());
    }

    @Test
    @DisplayName("Test updating an existing book")
    void testUpdateBook() throws IOException {
        Book updatedBook = new Book("A1234567890123", "Updated Book One", "Updated Description Description for this book and must have more than 20 characters", "Updated Publisher", LocalDate.of(2023, 1, 1), 59.99, 25);
        updatedBook.setBookId(books.get(0).getBookId());

        bookServiceTest.updateBook(updatedBook);

        Book loadedBook = bookServiceTest.findBookById(books.get(0).getBookId());
        assertEquals("Updated Book One", loadedBook.getBookTitle(), "The book title should match");
    }

    @Test
    @DisplayName("Test finding a book by ID")
    void testFindBookById() {
        Book loadedBook = bookServiceTest.findBookById(books.get(0).getBookId());
        assertEquals("Book One", loadedBook.getBookTitle(), "The book title should match");
    }

    @Test
    @DisplayName("Test retrieving all books")
    void testRetrievingAllBooks() throws IOException {
        List<Book> allBooks = bookServiceTest.getAllBooks();

        assertNotNull(allBooks, "The list of books should not be null.");
        assertEquals(3, allBooks.size(), "The number of books should be 3");
    }

    @Test
    @DisplayName("Test deleting an existing book")
    void testDeleteBook() throws IOException {
        bookFileManagerTest.delete(books.get(0).getBookId());

        List<Book> loadedBooks = bookFileManagerTest.load();
        assertEquals(2, loadedBooks.size(), "The total number of books should now be 2");
        assertThrows(IllegalArgumentException.class, () -> bookServiceTest.findBookById(books.get(0).getBookId()), "Book with the ID no longer exists");
    }

    @ParameterizedTest
    @DisplayName("Test finding a book with invalid ID")
    @ValueSource(ints = {999, 0, -1})
    void testFindBookWithInvalidId(int id) {
        assertThrows(IllegalArgumentException.class, () -> bookServiceTest.findBookById(id), "Should throw an exception");
    }

    @Test
    @DisplayName("Test validation failure for null book")
    void testSaveNullBook() {
        assertThrows(IllegalArgumentException.class, () -> bookServiceTest.saveBook(null), "Saving a null book should throw an exception");
    }

    @Test
    @DisplayName("Test updating a book that does not exist")
    void testUpdateNonExistentBook() {
        Book nonExistentBook = new Book("D1234567890123", "Non-Existent Book", "Description", "Publisher", LocalDate.of(2020, 1, 1), 19.99, 10);
        assertThrows(IllegalArgumentException.class, () -> bookServiceTest.updateBook(nonExistentBook));
    }
}