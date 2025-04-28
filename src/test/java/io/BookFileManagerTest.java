package io;



import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.model.Book;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BookFileManagerTest {

    private static final String tempFile = "src/test/resources/test_books_file.txt";
    private static BookFileManager bookFileManager;

    @BeforeEach
    void setUp() {
        bookFileManager = new BookFileManager(tempFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(tempFile));
    }



    @Test
    @DisplayName("Test saving and loading books")
    void testSaveAndLoadBooks() throws IOException {
        // Arrange
        List<Book> booksToSave = Arrays.asList(
                new Book("A1234567890123", "Book One", "Description One", "Publisher One", LocalDate.of(2023, 1, 1), 19.99, 10),
                new Book("A9876543210987", "Book Two", "Description Two", "Publisher Two", LocalDate.of(2022, 5, 15), 29.99, 5)
        );

        // Act
        bookFileManager.save(booksToSave);
        List<Book> loadedBooks = bookFileManager.load();

        // Assert
        assertEquals(booksToSave.size(), loadedBooks.size(), "The number of books should match");
        for (int i = 0; i < booksToSave.size(); i++) {
            assertEquals(booksToSave.get(i).getIsbn(), loadedBooks.get(i).getIsbn(), "ISBN should match");
            assertEquals(booksToSave.get(i).getBookTitle(), loadedBooks.get(i).getBookTitle(), "Book title should match");
            assertEquals(booksToSave.get(i).getDescription(), loadedBooks.get(i).getDescription(), "Description should match");
            assertEquals(booksToSave.get(i).getPublisher(), loadedBooks.get(i).getPublisher(), "Publisher should match");
            assertEquals(booksToSave.get(i).getPublicationDate(), loadedBooks.get(i).getPublicationDate(), "Publication date should match");
            assertEquals(booksToSave.get(i).getPrice(), loadedBooks.get(i).getPrice(), "Price should match");
            assertEquals(booksToSave.get(i).getStock(), loadedBooks.get(i).getStock(), "Stock should match");
        }
    }

    @Test
    @DisplayName("Test handling of empty book list")
    void testSaveAndLoadEmptyBooks() throws IOException {
        // Arrange
        List<Book> emptyBooks = Collections.emptyList();

        // Act
        bookFileManager.save(emptyBooks);
        List<Book> loadedBooks = bookFileManager.load();

        // Assert
        assertTrue(loadedBooks.isEmpty(), "Loaded books should be empty");
    }
}