package org.ignacioScript.co.service;

import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.BookValidator;

import java.io.IOException;
import java.util.List;

public class BookService {

    private final BookFileManager bookFileManager;
    private final String DEFAULT_FILE_PATH = "src/main/resources/books.csv";

    public BookService(BookFileManager bookFileManager) {
        this.bookFileManager = bookFileManager;
    }

    public BookService() {
        this.bookFileManager = new BookFileManager(DEFAULT_FILE_PATH);
    }


    public void saveBook(Book book) {
        validateBooks(book);

        bookFileManager.appendBook(book);
        FileLogger.logInfo("BookService - Book saved successfully");
    }

    public void updateBook(Book book) {
        validateBooks(book);

        Book updatedBook = findBookById(book.getBookId());
        if (updatedBook == null) {
            throw new IllegalArgumentException("Book with ID " + book.getBookId() + " does not exist");
        }

        bookFileManager.update(book);
        FileLogger.logInfo("BookService - Book updated successfully: " + book);

    }


    public void deleteBook(int id) {
        Book existingBook = findBookById(id);
        if (existingBook == null ) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }

        bookFileManager.delete(id);
        FileLogger.logInfo("BookService - Book deleted successfully: " + existingBook);
    }




    public Book findBookById(int id) {
        BookValidator.validateId(id);

        try {
            Book book = bookFileManager.getById(id);
            if (book == null) {
                throw new IllegalArgumentException("Book with ID: " + id + " does not exist");
            }

            FileLogger.logInfo("BookService - Successfully found book with ID: " + id);
            return book;

        }catch (RuntimeException e){
            FileLogger.logError("BookService - Error finding book: " + e.getMessage());
            throw e;
        }
    }


    public List<Book> getAllBooks() {

        try {
            FileLogger.logInfo("BookService - Fetching all books from file");
            return bookFileManager.load();
        }catch (IOException e) {
            FileLogger.logError("BookService - Error fetching books: " + e.getMessage());
            throw new RuntimeException("Failed to load book");
        }
    }




    private void validateBooks(Book book) {

        if (book == null) {
            throw  new IllegalArgumentException("Book cannot be null");
        }

        BookValidator.validateId(book.getBookId());
        BookValidator.validateIsbn(book.getIsbn());
        BookValidator.validatePrice(book.getPrice());
        BookValidator.validateTitle(book.getBookTitle());
        BookValidator.validatePublicationDate(book.getPublicationDate());
        BookValidator.validateStock(book.getStock());
        BookValidator.validateDescription(book.getDescription(), 200, 20);
    }
}
