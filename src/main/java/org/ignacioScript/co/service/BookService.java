package org.ignacioScript.co.service;

import org.ignacioScript.co.io.BookFileManager;
import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.AuthorBook;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.BookValidator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    private final BookFileManager bookFileManager;
    private final String DEFAULT_FILE_PATH = "src/main/resources/books.csv";

    public BookService(BookFileManager bookFileManager) {
        this.bookFileManager = bookFileManager;

        // Sync the ID counter with the highest existing ID in books.csv


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

    // Bubble sort books by title
    public List<Book> sortBooksByTitle(List<Book> books) {
        FileLogger.logInfo("BookService - Sorting books by title");
        for (int i = 0; i < books.size() - 1; i++) {
            for (int j = 0; j < books.size() - i - 1; j++) {
                if (books.get(j).getBookTitle().compareTo(books.get(j + 1).getBookTitle()) > 0) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
        return books;
    }

    // Quick sort books by title
    public List<Book> quickSortBooksByTitle(List<Book> books, int low, int high) {
        if (low < high) {
            int pi = partition(books, low, high);
            quickSortBooksByTitle(books, low, pi - 1);
            quickSortBooksByTitle(books, pi + 1, high);
        }
        return books;
    }

    private int partition(List<Book> books, int low, int high) {
        Book pivot = books.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (books.get(j).getBookTitle().compareTo(pivot.getBookTitle()) < 0) {
                i++;
                Book temp = books.get(i);
                books.set(i, books.get(j));
                books.set(j, temp);
            }
        }
        Book temp = books.get(i + 1);
        books.set(i + 1, books.get(high));
        books.set(high, temp);
        return i + 1;
    }


    public List<Book> searchBooksByKeyword(String keyword) {
        List<Book> books;
        List<Book> matchedBooks = new ArrayList<>();

        try {
            // Load all books from the file manager
            books = bookFileManager.load();

            // Iterate through each book and match the keyword
            for (Book book : books) {
                if (book.getBookTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        book.getDescription().toLowerCase().contains(keyword.toLowerCase())) {

                    // If a match is found, add the book to the matched list
                    matchedBooks.add(book);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to search books: " + e.getMessage(), e);
        }

        // Return the list of matched books
        return matchedBooks;
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
