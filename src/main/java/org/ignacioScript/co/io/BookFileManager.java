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
        FileLogger.log("Initialized BookFileManager for: " + filePath);
    }

    @Override
    public void save(List<Book> books){
        FileLogger.log("Starting to save " + books.size() + " books");

        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                writer.write(objectToString(book));
                writer.newLine();
                FileLogger.log("Save book: " + book.getBookTitle());
            }
            FileLogger.log("Successfully saved all books");

        }catch (IOException e) {
            FileLogger.log("ERROR saving books: " + e.getMessage());
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
