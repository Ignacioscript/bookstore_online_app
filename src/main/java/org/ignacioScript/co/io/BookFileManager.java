package org.ignacioScript.co.io;

import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.FileManagerValidator;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class BookFileManager extends FileManager <Book> {



    public BookFileManager(String filePath) {
        super(filePath);
        FileLogger.logInfo("Initialized BookFileManager for: " + filePath);
    }

    @Override
    public void save(List<Book> books){
        FileLogger.logInfo("Starting to save " + books.size() + " books");

        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                writer.write(objectToString(book));
                writer.newLine();
                FileLogger.logInfo("Save book: " + book.getBookTitle());
            }
            FileLogger.logInfo("Successfully saved all books");

        }catch (IOException e) {
            FileLogger.logError("ERROR saving books: " + e.getMessage());
            throw new RuntimeException("Save Operation failed", e);
        }

    }

    @Override
    public List<Book> load() throws IOException {
        List<Book> books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                books.add(stringToObject(line));
            }
        }
        return books;

    }

    @Override
    public void delete(int id) {
        FileLogger.logInfo("Attempting to delete book with ID: " + id);
        try {
            List<Book> books = load();
            Book bookToRemove = null;

            for (Book book : books) {
                if (book.getBookId() == id) {
                    bookToRemove = book;
                    break;
                }
            }

            if (bookToRemove != null) {
                books.remove(bookToRemove);
                save(books);
                FileLogger.logInfo("Successfully deleted book with ID: " + id);
            } else {
                FileLogger.logInfo("No Book found with ID: " + id);
            }
        } catch (IOException e) {
            FileLogger.logError("ERROR deleting book: " + e.getMessage());
        }
    }

    @Override
    public void update(Book book) {
        FileLogger.logInfo("Attempting to update bok with ID: " + book.getBookId());
        try {
            List<Book> books = load();
            boolean updated = false;

            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getBookId() == book.getBookId()) {
                    books.set(i, book);
                    updated = true;
                    break;

                }
            }

            if (updated) {
                save(books);
                FileLogger.logInfo("Successfully updated book with ID: " + book.getBookId());
            }else {
                FileLogger.logInfo("NO book found with ID: " + book.getBookId());
                throw new RuntimeException("Book with ID: " + book.getBookId() + " Not found");
            }

        }catch (IOException e) {
            FileLogger.logError("ERROR updating book: " + e.getMessage());
        }


    }

    @Override
    public Book getById(int id) {
        try {
            List<Book> books = load(); // or however you load your books
            for (Book book : books) {
                if (book.getBookId() == id) {
                    return book;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null; // or throw an exception
    }

    @Override
    protected String objectToString(Book book) {
        return String.join(",",
                String.valueOf(book.getBookId()),
                book.getIsbn(),
                book.getBookTitle(),
                book.getDescription(),
                book.getPublisher(),
                book.getPublicationDate().toString(),
                book.getPrice().toString(),
                book.getStock().toString()
        );

    }

    @Override
    protected Book stringToObject(String line) {
        String[] parts = line.split(",");
        Book book = new Book(
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                LocalDate.parse(parts[5]),
                Double.parseDouble(parts[6]),
                Integer.parseInt(parts[7])
        );
        book.setBookId(Integer.parseInt(parts[0])); // ← 🔥 Set the ID from the file!
        return book;
    }


}
