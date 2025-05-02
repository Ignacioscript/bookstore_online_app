package org.ignacioScript.co.io;


import org.ignacioScript.co.model.Author;
import org.ignacioScript.co.model.AuthorBook;
import org.ignacioScript.co.model.Book;
import org.ignacioScript.co.util.FileLogger;
import org.ignacioScript.co.validation.FileManagerValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorBookFileManager extends FileManager<AuthorBook> {

    private final BookFileManager bookFileManager;
    private final AuthorFileManager authorFileManager;

    public AuthorBookFileManager(String filePath, String bookFilePath, String authorFilePath) {
        super(filePath);
        this.bookFileManager = new BookFileManager(bookFilePath);
        this.authorFileManager = new AuthorFileManager(authorFilePath);
        FileLogger.logInfo("AuthorBookFileManager initialized.");
    }


    @Override
    public void save(List<AuthorBook> authorBooks) {
        FileLogger.logInfo("Saving author-book relationships to file.");
        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (AuthorBook authorBook : authorBooks) {
                writer.write(objectToString(authorBook));
                writer.newLine();
            }

        }catch (IOException e) {
            FileLogger.logError("ERROR saving author-book relationships.");
            throw new RuntimeException("Save operation failed: " + e.getMessage(), e);
        }
    }

    public void append(AuthorBook authorBook) {
        FileLogger.logInfo("Appending a new author-book relationship.");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(objectToString(authorBook));
            writer.newLine();
        } catch (IOException e) {
            FileLogger.logError("ERROR appending a new author-book relationship: " + e.getMessage());
            throw new RuntimeException("Append operation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AuthorBook> load() throws IOException {
        FileLogger.logInfo("Loading author-book relationships from file.");
        List<AuthorBook> authorBooks = new ArrayList<>();
        FileManagerValidator.validateExistingFile(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                authorBooks.add(stringToObject(line));
            }
        }
        return authorBooks;
    }

    @Override
    public void delete(int id) {
        FileLogger.logInfo("Deleting author-book relationship with ID: " + id);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<AuthorBook> authorBooks = load();
            AuthorBook authorBookToDelete = null;

            for (AuthorBook authorBook : authorBooks) {
                if (authorBook.getAuthor().getAuthorId() == id) {
                    authorBookToDelete = authorBook;
                    break;
                }
            }

            if (authorBookToDelete != null) {
                authorBooks.remove(authorBookToDelete);
                save(authorBooks);
                FileLogger.logInfo("Author-book relationship deleted successfully.");
            } else {
                FileLogger.logError("Author-book relationship not found.");
            }

        } catch (IOException e) {
            FileLogger.logError("ERROR deleting author-book relationship: " + e.getMessage());
            throw new RuntimeException("Delete operation failed: " + e.getMessage(), e);
        }

    }

    @Override
    public void update(AuthorBook authorBook) {
        FileLogger.logInfo("Updating author-book relationship.");
        try {
            List<AuthorBook> authorBooks = load();
            for (int i = 0; i < authorBooks.size(); i++) {
                if (authorBooks.get(i).getAuthor().getAuthorId() == authorBook.getAuthor().getAuthorId()) {
                    authorBooks.set(i, authorBook);
                    break;
                }
            }
            save(authorBooks);
            FileLogger.logInfo("Author-book relationship updated successfully.");
        } catch (IOException e) {
            FileLogger.logError("ERROR updating author-book relationship: " + e.getMessage());
            throw new RuntimeException("Update operation failed: " + e.getMessage(), e);
        }

    }

    @Override
    public AuthorBook getById(int id) {
        FileLogger.logInfo("Getting author-book relationship by ID: " + id);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AuthorBook authorBook = stringToObject(line);
                if (authorBook.getAuthor().getAuthorId() == id) {
                    return authorBook;
                }
            }
        } catch (IOException e) {
            FileLogger.logError("ERROR getting author-book relationship: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected String objectToString(AuthorBook authorBook) {
        return String.join(",",
                String.valueOf(authorBook.getAuthor().getAuthorId()),
                String.valueOf(authorBook.getBook().getBookId())
                );
    }

    @Override
    protected AuthorBook stringToObject(String line) {
        String[] parts = line.split(",");
        if (parts.length == 2) {
            int authorId = Integer.parseInt(parts[0]);
            int bookId = Integer.parseInt(parts[1]);

            Author author = authorFileManager.getById(authorId);
            Book book = bookFileManager.getById(bookId);

            return new AuthorBook(book, author);
        }

        return null;
    }
}